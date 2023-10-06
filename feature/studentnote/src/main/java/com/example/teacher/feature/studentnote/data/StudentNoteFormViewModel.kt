package com.example.teacher.feature.studentnote.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.studentnote.StudentNoteRepository
import com.example.teacher.core.domain.GenerateNoteTitleUseCase
import com.example.teacher.core.model.data.StudentNote
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.studentnote.nav.StudentNoteNavigation
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
internal class StudentNoteFormViewModel @Inject constructor(
    private val repository: StudentNoteRepository,
    private val savedStateHandle: SavedStateHandle,
    generateNoteTitleUseCase: GenerateNoteTitleUseCase,
) : ViewModel() {

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    private val studentNoteId = savedStateHandle.getStateFlow(STUDENT_NOTE_ID_KEY, 0L)
    val isStudentNoteDeleted = savedStateHandle.getStateFlow(IS_STUDENT_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentNoteResult: StateFlow<Result<StudentNote?>> = studentNoteId
        .flatMapLatest { studentNoteId -> repository.getStudentNoteOrNullById(studentNoteId) }
        .stateIn(initialValue = Result.Loading)

    private val _form =
        MutableStateFlow(StudentNoteFormProvider.createDefaultForm(title = generateNoteTitleUseCase()))
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentFullName: StateFlow<String?> = studentId
        .flatMapLatest { studentId -> repository.getStudentFullNameNameById(studentId = studentId) }
        .stateIn(initialValue = null)

    init {
        studentNoteResult
            .onEach { studentNoteResult ->
                val studentNote = (studentNoteResult as? Result.Success)?.data ?: return@onEach

                _form.value = form.value.copy(
                    title = StudentNoteFormProvider.validateTitle(studentNote.title),
                    description = StudentNoteFormProvider.validateDescription(studentNote.description),
                    isNegative = studentNote.isNegative,
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onTitleChange(title: String) {
        _form.value = form.value.copy(title = StudentNoteFormProvider.validateTitle(title))
    }

    fun onDescriptionChange(description: String) {
        _form.value =
            form.value.copy(description = StudentNoteFormProvider.validateDescription(description))
    }

    fun onIsNoteNegativeChange(isNegative: Boolean) {
        _form.value = form.value.copy(isNegative = isNegative)
    }

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateStudentNote(
                id = studentNoteId.value.let { id -> if (id != 0L) id else null },
                studentId = studentId.value,
                title = form.title.value.trim(),
                description = form.description.value?.trim().orEmpty(),
                isNegative = form.isNegative,
            )

            if (isActive) {
                _form.value = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDeleteStudentNote() {
        viewModelScope.launch {
            repository.deleteStudentNoteById(studentNoteId.value)
            savedStateHandle[IS_STUDENT_DELETED_KEY] = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val STUDENT_ID_KEY = StudentNoteNavigation.studentIdArg
        private const val STUDENT_NOTE_ID_KEY = StudentNoteNavigation.studentNoteIdArg
        private const val IS_STUDENT_DELETED_KEY = "is-student-deleted"
    }
}