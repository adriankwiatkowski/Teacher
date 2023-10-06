package com.example.teacher.feature.note.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.note.NoteRepository
import com.example.teacher.core.domain.GenerateNoteTitleUseCase
import com.example.teacher.core.model.data.Note
import com.example.teacher.core.model.data.NotePriority
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.note.nav.NoteNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NoteFormViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
    generateNoteTitleUseCase: GenerateNoteTitleUseCase,
) : ViewModel() {

    private val noteId = savedStateHandle.getStateFlow(NOTE_ID_KEY, 0L)
    val isNoteDeleted = savedStateHandle.getStateFlow(IS_NOTE_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val noteResult: StateFlow<Result<Note?>> = noteId
        .flatMapLatest { noteId -> repository.getNoteOrNullById(noteId) }
        .stateIn(initialValue = Result.Loading)

    private val _form =
        MutableStateFlow(NoteFormProvider.createDefaultForm(title = generateNoteTitleUseCase()))
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    init {
        noteResult
            .onEach { noteResource ->
                val note = (noteResource as? Result.Success)?.data ?: return@onEach

                _form.value = form.value.copy(
                    title = NoteFormProvider.validateTitle(note.title),
                    text = NoteFormProvider.validateText(note.text),
                    priority = note.priority,
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onTitleChange(title: String) {
        _form.value = form.value.copy(title = NoteFormProvider.validateTitle(title))
    }

    fun onTextChange(description: String) {
        _form.value = form.value.copy(text = NoteFormProvider.validateText(description))
    }

    fun onPriorityChange(priority: NotePriority) {
        _form.value = form.value.copy(priority = priority)
    }

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateNote(
                id = noteId.value.let { id -> if (id != 0L) id else null },
                title = form.title.value.trim(),
                text = form.text.value.trim(),
                priority = form.priority,
            )

            if (isActive) {
                _form.value = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDeleteNote() {
        viewModelScope.launch {
            repository.deleteNoteById(noteId.value)
            savedStateHandle[IS_NOTE_DELETED_KEY] = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val NOTE_ID_KEY = NoteNavigation.noteIdArg
        private const val IS_NOTE_DELETED_KEY = "is-note-deleted"
    }
}