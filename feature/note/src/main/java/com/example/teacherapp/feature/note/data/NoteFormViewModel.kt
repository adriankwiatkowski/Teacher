package com.example.teacherapp.feature.note.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.note.NoteRepository
import com.example.teacherapp.core.model.data.Note
import com.example.teacherapp.core.model.data.NotePriority
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.note.nav.NoteNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
) : ViewModel() {

    private val noteId = savedStateHandle.getStateFlow(NOTE_ID_KEY, 0L)
    val isNoteDeleted = savedStateHandle.getStateFlow(IS_NOTE_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val noteResult: StateFlow<Result<Note?>> = noteId
        .flatMapLatest { noteId -> repository.getNoteOrNullById(noteId) }
        .stateIn(initialValue = Result.Loading)

    var form by mutableStateOf(NoteFormProvider.createDefaultForm())
        private set

    init {
        noteResult
            .onEach { noteResource ->
                val note = (noteResource as? Result.Success)?.data
                if (note == null) {
                    form = NoteFormProvider.createDefaultForm(status = FormStatus.Idle)
                    return@onEach
                }

                form = form.copy(
                    title = NoteFormProvider.validateTitle(note.title),
                    text = NoteFormProvider.validateText(note.text),
                    priority = note.priority,
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onTitleChange(title: String) {
        form = form.copy(title = NoteFormProvider.validateTitle(title))
    }

    fun onTextChange(description: String) {
        form = form.copy(text = NoteFormProvider.validateText(description))
    }

    fun onPriorityChange(priority: NotePriority) {
        form = form.copy(priority = priority)
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateNote(
                id = noteId.value.let { id -> if (id != 0L) id else null },
                title = form.title.value.trim(),
                text = form.text.value.trim(),
                priority = form.priority,
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
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