package com.example.teacherapp.ui.screens.student.data

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.student.StudentDataSource
import com.example.teacherapp.data.models.*
import com.example.teacherapp.data.models.entities.*
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentCreatorViewModel @Inject constructor(
    private val studentDataSource: StudentDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId: StateFlow<Long> = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    private val schoolClassId: StateFlow<Long> =
        savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)

    private val resourceStatus = MutableStateFlow<ResourceStatus>(ResourceStatus.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val student: Flow<Student?> = studentId
        .flatMapLatest { id ->
            resourceStatus.value = ResourceStatus.Loading
            val result = studentDataSource.getStudentById(id)
            resourceStatus.value = ResourceStatus.Success
            result
        }
        .distinctUntilChanged()
        .catch { e ->
            if (e !is SQLiteException) {
                throw e
            }

            resourceStatus.value = ResourceStatus.Error
        }

    var form by mutableStateOf(StudentFormProvider.createDefaultForm())
        private set

    val studentResource: StateFlow<Resource<Student?>> =
        combine(resourceStatus, student) { status, student ->
            when (status) {
                ResourceStatus.Loading -> Resource.Loading
                ResourceStatus.Error -> Resource.Error(NoSuchElementException())
                ResourceStatus.Success -> Resource.Success(student)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Resource.Loading,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassName: StateFlow<String?> = schoolClassId.flatMapLatest { schoolClassId ->
        studentDataSource.getStudentSchoolClassNameById(schoolClassId = schoolClassId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = null
    )

    init {
        student
            .onEach { student ->
                if (student == null) {
                    form = StudentFormProvider.createDefaultForm(status = FormStatus.Idle)
                    return@onEach
                }

                form = form.copy(
                    id = student.id,
                    name = StudentFormProvider.validateName(student.name),
                    surname = StudentFormProvider.validateSurname(student.surname),
                    email = StudentFormProvider.validateEmail(student.email),
                    phone = StudentFormProvider.validatePhone(student.phone),
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onNameChange(name: String) {
        form = form.copy(name = StudentFormProvider.validateName(name))
    }

    fun onSurnameChange(surname: String) {
        form = form.copy(surname = StudentFormProvider.validateSurname(surname))
    }

    fun onEmailChange(email: String) {
        form = form.copy(email = StudentFormProvider.validateEmail(email))
    }

    fun onPhoneChange(phone: String) {
        form = form.copy(phone = StudentFormProvider.validatePhone(phone))
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

            val id = form.id
            val name = form.name.value.trim()
            val surname = form.surname.value.trim()
            val email = form.email.value?.trim()
            val phone = form.phone.value?.trim()

            form = form.copy(status = FormStatus.Saving)

            studentDataSource.insertOrUpdateStudent(
                id = id,
                schoolClassId = schoolClassId.value,
                orderInClass = null,
                name = name,
                surname = surname,
                email = email,
                phone = phone,
            )

            form = form.copy(status = FormStatus.Success)
        }
    }

    companion object {
        private const val STUDENT_ID_KEY = TeacherDestinationsArgs.STUDENT_ID_ARG
        private const val SCHOOL_CLASS_ID_KEY = TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG
    }
}