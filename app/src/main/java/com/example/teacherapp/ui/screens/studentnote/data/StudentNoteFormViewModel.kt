package com.example.teacherapp.ui.screens.studentnote.data

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.student.note.StudentNoteDataSource
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.ResourceStatus
import com.example.teacherapp.data.models.entities.StudentNote
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentNoteFormViewModel @Inject constructor(
    private val studentNoteDataSource: StudentNoteDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val resourceStatus = MutableStateFlow<ResourceStatus>(ResourceStatus.Loading)

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    private val studentNoteId = savedStateHandle.getStateFlow(STUDENT_NOTE_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val studentNote: Flow<StudentNote?> = studentNoteId
        .flatMapLatest { id ->
            resourceStatus.value = ResourceStatus.Loading
            studentNoteDataSource.getStudentNoteById(id).also {
                resourceStatus.value = ResourceStatus.Success
            }
        }
        .distinctUntilChanged()
        .catch { e ->
            if (e !is SQLiteException) {
                throw e
            }

            resourceStatus.value = ResourceStatus.Error
        }

    var form by mutableStateOf(StudentNoteFormProvider.createDefaultForm())
        private set

    val studentNoteResource: StateFlow<Resource<StudentNote?>> =
        combine(resourceStatus, studentNote) { status, studentNote ->
            when (status) {
                ResourceStatus.Loading -> Resource.Loading
                ResourceStatus.Error -> Resource.Error(NoSuchElementException())
                ResourceStatus.Success -> Resource.Success(studentNote)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Resource.Loading,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentFullName: StateFlow<String?> = studentId.flatMapLatest { studentId ->
        studentNoteDataSource.getStudentFullNameNameById(studentId = studentId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = null
    )

    var isStudentDeleted by mutableStateOf(false)

    init {
        studentNote
            .onEach { studentNote ->
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
        if (!form.isValid) {
            return
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            form = form.copy(status = FormStatus.Error)
            if (exception !is SQLiteException) {
                throw exception
            }
        }
        viewModelScope.launch(handler) {
            if (form.status == FormStatus.Saving || form.status == FormStatus.Success) {
                return@launch
            }

            val id = studentNoteId.value.let { id ->
                if (id == 0L) null else id
            }
            val studentId = studentId.value
            val title = form.title.value.trim()
            val description = form.description.value?.trim()

            form = form.copy(status = FormStatus.Saving)

            studentNoteDataSource.insertOrUpdateStudentNote(
                id = id,
                studentId = studentId,
                title = title,
                description = description.orEmpty(),
                isNegative = true,
            )

            form = form.copy(status = FormStatus.Success)
        }
    }

    fun onDeleteStudentNote() {
        viewModelScope.launch {
            isStudentDeleted = false

            studentNoteDataSource.deleteStudentNoteById(studentNoteId.value)

            isStudentDeleted = true
        }
    }

    companion object {
        private const val STUDENT_NOTE_ID_KEY = TeacherDestinationsArgs.STUDENT_NOTE_ID_ARG
        private const val STUDENT_ID_KEY = TeacherDestinationsArgs.STUDENT_ID_ARG
    }
}