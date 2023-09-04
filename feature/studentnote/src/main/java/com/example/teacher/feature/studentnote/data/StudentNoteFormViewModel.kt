package com.example.teacher.feature.studentnote.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.studentnote.StudentNoteRepository
import com.example.teacher.core.model.data.StudentNote
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.studentnote.nav.StudentNoteNavigation
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
internal class StudentNoteFormViewModel @Inject constructor(
    private val repository: StudentNoteRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    private val studentNoteId = savedStateHandle.getStateFlow(STUDENT_NOTE_ID_KEY, 0L)
    val isStudentNoteDeleted = savedStateHandle.getStateFlow(IS_STUDENT_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentNoteResult: StateFlow<Result<StudentNote?>> = studentNoteId
        .flatMapLatest { studentNoteId -> repository.getStudentNoteOrNullById(studentNoteId) }
        .stateIn(initialValue = Result.Loading)

    var form by mutableStateOf(StudentNoteFormProvider.createDefaultForm())
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentFullName: StateFlow<String?> = studentId
        .flatMapLatest { studentId -> repository.getStudentFullNameNameById(studentId = studentId) }
        .stateIn(initialValue = null)

    init {
        studentNoteResult
            .onEach { studentNoteResource ->
                val studentNote = (studentNoteResource as? Result.Success)?.data
                if (studentNote == null) {
                    form = StudentNoteFormProvider.createDefaultForm(status = FormStatus.Idle)
                    return@onEach
                }

                form = form.copy(
                    title = StudentNoteFormProvider.validateTitle(studentNote.title),
                    description = StudentNoteFormProvider.validateDescription(studentNote.description),
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onTitleChange(title: String) {
        form = form.copy(title = StudentNoteFormProvider.validateTitle(title))
    }

    fun onDescriptionChange(description: String) {
        form = form.copy(description = StudentNoteFormProvider.validateDescription(description))
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateStudentNote(
                id = studentNoteId.value.let { id -> if (id != 0L) id else null },
                studentId = studentId.value,
                title = form.title.value.trim(),
                description = form.description.value?.trim().orEmpty(),
                isNegative = true,
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
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