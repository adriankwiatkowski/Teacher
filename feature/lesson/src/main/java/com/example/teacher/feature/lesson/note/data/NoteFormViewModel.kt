package com.example.teacher.feature.lesson.note.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.lessonnote.LessonNoteRepository
import com.example.teacher.core.domain.GenerateNoteTitleUseCase
import com.example.teacher.core.model.data.LessonNote
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.lesson.nav.LessonNavigation
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
    private val repository: LessonNoteRepository,
    private val savedStateHandle: SavedStateHandle,
    generateNoteTitleUseCase: GenerateNoteTitleUseCase,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)
    private val noteId = savedStateHandle.getStateFlow(LESSON_NOTE_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_NOTE_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val noteResult: StateFlow<Result<LessonNote?>> = noteId
        .flatMapLatest { noteId -> repository.getLessonNoteOrNullById(noteId) }
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

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateLessonNote(
                id = noteId.value.let { id -> if (id != 0L) id else null },
                lessonId = lessonId.value,
                title = form.title.value.trim(),
                text = form.text.value.trim(),
            )

            if (isActive) {
                _form.value = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDeleteNote() {
        viewModelScope.launch {
            repository.deleteLessonNoteById(noteId.value)
            savedStateHandle[IS_NOTE_DELETED_KEY] = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
        private const val LESSON_NOTE_ID_KEY = LessonNavigation.lessonNoteIdArg
        private const val IS_NOTE_DELETED_KEY = "is-note-deleted"
    }
}