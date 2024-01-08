package com.example.teacher.feature.student.data

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _form =
        MutableStateFlow(StudentFormProvider.createDefaultForm(usedRegisterNumbers = null))
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    init {
        // Revalidate register number when used register numbers are loaded.
        usedRegisterNumbersWithoutCurrentStudent
            .onEach { _ ->
                onRegisterNumberChange(
                    registerNumber = form.value.registerNumber.value,
                    isHumanInput = false,
                )
            }
            .launchIn(viewModelScope)

        // If updating, fill form with information saved in repository.
        studentResult
            .onEach { studentResult ->
                val student = (studentResult as? Result.Success)?.data ?: return@onEach

                _form.value = form.value.copy(
                    id = student.id,
                    name = StudentFormProvider.validateName(student.name),
                    surname = StudentFormProvider.validateSurname(student.surname),
                    email = StudentFormProvider.validateEmail(student.email),
                    phone = StudentFormProvider.validatePhone(student.phone),
                    registerNumber = StudentFormProvider.validateRegisterNumber(
                        registerNumber = student.registerNumber.toString(),
                        usedRegisterNumbers = usedRegisterNumbersWithoutCurrentStudent.value,
                    ),
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onNameChange(name: String) {
        _form.value = form.value.copy(name = StudentFormProvider.validateName(name))
    }

    fun onSurnameChange(surname: String) {
        _form.value = form.value.copy(surname = StudentFormProvider.validateSurname(surname))
    }

    fun onEmailChange(email: String) {
        _form.value = form.value.copy(email = StudentFormProvider.validateEmail(email))
    }

    fun onPhoneChange(phone: String) {
        _form.value = form.value.copy(phone = StudentFormProvider.validatePhone(phone))
    }

    fun onRegisterNumberChange(registerNumber: String?) =
        onRegisterNumberChange(registerNumber = registerNumber, isHumanInput = true)

    private fun onRegisterNumberChange(registerNumber: String?, isHumanInput: Boolean) {
        val validatedRegisterNumber = StudentFormProvider.validateRegisterNumber(
            registerNumber = registerNumber,
            usedRegisterNumbers = usedRegisterNumbersWithoutCurrentStudent.value,
        )

        _form.value = form.value.copy(registerNumber = validatedRegisterNumber)

        if (!isHumanInput) {
            initialForm.value = initialForm.value.copy(registerNumber = validatedRegisterNumber)
        }
    }

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.upsertStudent(
                id = form.id,
                schoolClassId = schoolClassId.value,
                registerNumber = form.registerNumber.value?.trim()?.toLongOrNull(),
                name = form.name.value.trim(),
                surname = form.surname.value.trim(),
                email = form.email.value?.trim(),
                phone = form.phone.value?.trim(),
            )

            if (isActive) {
                _form.value = form.copy(status = FormStatus.Success)
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