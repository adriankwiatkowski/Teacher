package com.example.teacherapp.ui.screens.student.note.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.repository.StudentNoteRepository
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.StudentNote
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentNoteFormViewModel @Inject constructor(
    private val repository: StudentNoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    private val studentNoteId = savedStateHandle.getStateFlow(STUDENT_NOTE_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentNoteResource: StateFlow<Resource<StudentNote?>> = studentNoteId
        .flatMapLatest { studentNoteId -> repository.getStudentNoteByIdOrNull(studentNoteId) }
        .stateIn(initialValue = Resource.Loading)

    var form by mutableStateOf(StudentNoteFormProvider.createDefaultForm())
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentFullName: StateFlow<String?> = studentId
        .flatMapLatest { studentId -> repository.getStudentFullNameNameById(studentId = studentId) }
        .stateIn(initialValue = null)

    var isStudentNoteDeleted by mutableStateOf(false)

    init {
        studentNoteResource
            .onEach { studentNoteResource ->
                val studentNote = (studentNoteResource as? Resource.Success)?.data
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
        if (!form.canSubmit) {
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
            isStudentNoteDeleted = false
            repository.deleteStudentNoteById(studentNoteId.value)
            isStudentNoteDeleted = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
        private const val STUDENT_NOTE_ID_KEY = StudentNavigation.studentNoteIdArg
    }
}