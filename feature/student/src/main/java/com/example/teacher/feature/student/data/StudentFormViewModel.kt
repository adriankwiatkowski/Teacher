package com.example.teacher.feature.student.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.student.StudentRepository
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.student.nav.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class StudentFormViewModel @Inject constructor(
    private val repository: StudentRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId: StateFlow<Long> = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    private val schoolClassId: StateFlow<Long> =
        savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentResult: StateFlow<Result<Student?>> = studentId
        .flatMapLatest { studentId -> repository.getStudentOrNullById(studentId) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val usedRegisterNumbers: StateFlow<List<Long>?> = schoolClassId
        .flatMapLatest { schoolClassId ->
            repository.getUsedRegisterNumbersBySchoolClassId(schoolClassId)
        }
        .mapLatest { usedRegisterNumbersResult ->
            (usedRegisterNumbersResult as? Result.Success)?.data
        }
        .stateIn(initialValue = null)

    private val usedRegisterNumbersWithoutCurrentStudent: StateFlow<List<Long>?> =
        combine(usedRegisterNumbers, studentResult) { usedRegisterNumbers, studentResult ->
            if (usedRegisterNumbers == null) {
                return@combine null
            }
            if (studentResult !is Result.Success) {
                return@combine null
            }

            val studentRegisterNumber =
                studentResult.data?.registerNumber ?: return@combine usedRegisterNumbers

            return@combine usedRegisterNumbers.filter { number -> number != studentRegisterNumber }
        }.stateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassName: StateFlow<String?> = schoolClassId
        .flatMapLatest { schoolClassId -> repository.getStudentSchoolClassNameById(schoolClassId) }
        .stateIn(initialValue = null)

    var form by mutableStateOf(StudentFormProvider.createDefaultForm(usedRegisterNumbers = null))
        private set

    init {
        // Revalidate register number when used register numbers are loaded.
        usedRegisterNumbersWithoutCurrentStudent
            .onEach { _ -> onRegisterNumberChange(form.registerNumber.value) }
            .launchIn(viewModelScope)

        // If updating, fill form with information saved in repository.
        studentResult
            .onEach { studentResult ->
                val student = (studentResult as? Result.Success)?.data ?: return@onEach

                form = form.copy(
                    id = student.id,
                    name = StudentFormProvider.validateName(student.name),
                    surname = StudentFormProvider.validateSurname(student.surname),
                    email = StudentFormProvider.validateEmail(student.email),
                    phone = StudentFormProvider.validatePhone(student.phone),
                    registerNumber = StudentFormProvider.validateRegisterNumber(
                        registerNumber = student.registerNumber.toString(),
                        usedRegisterNumbers = usedRegisterNumbersWithoutCurrentStudent.value,
                    ),
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

    fun onRegisterNumberChange(registerNumber: String?) {
        form = form.copy(
            registerNumber = StudentFormProvider.validateRegisterNumber(
                registerNumber = registerNumber,
                usedRegisterNumbers = usedRegisterNumbersWithoutCurrentStudent.value,
            )
        )
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateStudent(
                id = form.id,
                schoolClassId = schoolClassId.value,
                registerNumber = form.registerNumber.value?.trim()?.toLongOrNull(),
                name = form.name.value.trim(),
                surname = form.surname.value.trim(),
                email = form.email.value?.trim(),
                phone = form.phone.value?.trim(),
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
            }
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
        private const val SCHOOL_CLASS_ID_KEY = StudentNavigation.schoolClassIdArg
    }
}