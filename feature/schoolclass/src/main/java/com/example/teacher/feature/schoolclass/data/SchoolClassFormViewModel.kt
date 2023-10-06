package com.example.teacher.feature.schoolclass.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
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
internal class SchoolClassFormViewModel @Inject constructor(
    private val repository: SchoolClassRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val schoolClassId = savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassResult: StateFlow<Result<BasicSchoolClass?>> = schoolClassId
        .flatMapLatest { schoolClassId -> repository.getBasicSchoolClassOrNullById(schoolClassId) }
        .stateIn(initialValue = Result.Loading)

    private val _form = MutableStateFlow(SchoolClassFormProvider.createDefaultForm())
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    val schoolYears = repository.getAllSchoolYears()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList(),
        )

    init {
        schoolYears
            .onEach { schoolYears ->
                val defaultSchoolYear = schoolYears
                    .firstOrNull { schoolYear ->
                        schoolYear.id == form.value.schoolYear.value?.id
                    } ?: schoolYears.firstOrNull()
                onSchoolYearChange(schoolYear = defaultSchoolYear, isHumanInput = false)
            }
            .launchIn(viewModelScope)

        schoolClassResult
            .onEach { schoolClassResult ->
                val schoolClass = (schoolClassResult as? Result.Success)?.data
                if (schoolClass == null) {
                    _form.value = SchoolClassFormProvider.createDefaultForm(
                        schoolYear = form.value.schoolYear.value,
                    )
                    initialForm.value = form.value
                    return@onEach
                }

                _form.value = form.value.copy(
                    schoolClassName = SchoolClassFormProvider.validateSchoolClassName(schoolClass.name),
                    schoolYear = SchoolClassFormProvider.validateSchoolYear(schoolClass.schoolYear),
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onSchoolClassNameChange(name: String) {
        _form.value =
            form.value.copy(schoolClassName = SchoolClassFormProvider.validateSchoolClassName(name))
    }

    fun onSchoolYearChange(schoolYear: SchoolYear?) =
        onSchoolYearChange(schoolYear = schoolYear, isHumanInput = true)

    private fun onSchoolYearChange(schoolYear: SchoolYear?, isHumanInput: Boolean) {
        val validatedSchoolYear = SchoolClassFormProvider.validateSchoolYear(schoolYear)
        _form.value = form.value.copy(schoolYear = validatedSchoolYear)

        if (!isHumanInput) {
            initialForm.value = initialForm.value.copy(schoolYear = validatedSchoolYear)
        }
    }

    fun onSubmit() {
        if (!form.value.isSubmitEnabled) {
            return
        }

        _form.value = form.value.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateSchoolClass(
                id = schoolClassId.value.let { id -> if (id != 0L) id else null },
                schoolYearId = form.value.schoolYear.value!!.id,
                name = form.value.schoolClassName.value,
            )

            if (isActive) {
                _form.value = form.value.copy(status = FormStatus.Success)
            }
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val SCHOOL_CLASS_ID_KEY = SchoolClassNavigation.schoolClassIdArg
    }
}