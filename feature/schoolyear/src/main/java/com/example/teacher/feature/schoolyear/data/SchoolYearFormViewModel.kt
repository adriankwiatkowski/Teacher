package com.example.teacher.feature.schoolyear.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolyear.SchoolYearRepository
import com.example.teacher.core.domain.GenerateSchoolYearNameUseCase
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.schoolyear.nav.SchoolYearNavigation
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class SchoolYearFormViewModel @Inject constructor(
    private val repository: SchoolYearRepository,
    private val savedStateHandle: SavedStateHandle,
    generateSchoolYearNameUseCase: GenerateSchoolYearNameUseCase,
) : ViewModel() {

    private val schoolYearId = savedStateHandle.getStateFlow(SCHOOL_YEAR_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolYearResult: StateFlow<Result<SchoolYear?>> = schoolYearId
        .flatMapLatest { schoolYearId -> repository.getSchoolYearById(schoolYearId) }
        .stateIn(initialValue = Result.Loading)

    private val _form =
        MutableStateFlow(SchoolYearFormProvider.createDefaultForm(generateSchoolYearNameUseCase))
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    init {
        schoolYearResult
            .onEach { schoolYearResult ->
                val schoolYear = (schoolYearResult as? Result.Success)?.data
                if (schoolYear == null) {
                    _form.value =
                        SchoolYearFormProvider.createDefaultForm(generateSchoolYearNameUseCase)
                    initialForm.value = form.value
                    return@onEach
                }

                _form.value = form.value.copy(
                    schoolYearName = SchoolYearFormProvider.validateSchoolYearName(schoolYear.name),
                    termForms = listOf(schoolYear.firstTerm, schoolYear.secondTerm).map { term ->
                        TermForm(
                            name = SchoolYearFormProvider.validateTermName(term.name),
                            startDate = SchoolYearFormProvider.validateStartDate(term.startDate),
                            endDate = SchoolYearFormProvider.validateEndDate(term.endDate),
                        )
                    },
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onSchoolYearNameChange(name: String) {
        _form.value =
            form.value.copy(schoolYearName = SchoolYearFormProvider.validateSchoolYearName(name))
    }

    fun onTermNameChange(index: Int, name: String) {
        val oldTerms = form.value.termForms
        val newTerms = oldTerms.toMutableList()
        newTerms[index] = newTerms[index].copy(name = SchoolYearFormProvider.validateTermName(name))

        _form.value = form.value.copy(termForms = newTerms)
    }

    fun onStartDateChange(index: Int, date: LocalDate) {
        val oldTerms = form.value.termForms
        val newTerms = oldTerms.toMutableList()
        newTerms[index] =
            newTerms[index].copy(startDate = SchoolYearFormProvider.validateStartDate(date))

        _form.value = form.value.copy(termForms = newTerms)
        sanitizeDates(changedIndex = index, isStartDateChanged = true)
    }

    fun onEndDateChange(index: Int, date: LocalDate) {
        val oldTerms = form.value.termForms
        val newTerms = oldTerms.toMutableList()
        newTerms[index] =
            newTerms[index].copy(endDate = SchoolYearFormProvider.validateEndDate(date))

        _form.value = form.value.copy(termForms = newTerms)
        sanitizeDates(changedIndex = index, isStartDateChanged = false)
    }

    fun onAddSchoolYear() {
        if (!form.value.isSubmitEnabled) {
            return
        }

        _form.value = form.value.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            val firstTerm = form.value.termForms[0]
            val secondTerm = form.value.termForms[1]
            val schoolYearName = form.value.schoolYearName.value

            repository.upsertSchoolYear(
                id = schoolYearId.value.let { id -> if (id != 0L) id else null },
                schoolYearName = schoolYearName,
                termFirstName = firstTerm.name.value,
                termFirstStartDate = firstTerm.startDate.date,
                termFirstEndDate = firstTerm.endDate.date,
                termSecondName = secondTerm.name.value,
                termSecondStartDate = secondTerm.startDate.date,
                termSecondEndDate = secondTerm.endDate.date,
            )

            if (isActive) {
                _form.value = form.value.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDeleteSchoolYear() {
        viewModelScope.launch {
            repository.deleteSchoolYearById(schoolYearId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    private fun sanitizeDates(
        changedIndex: Int,
        isStartDateChanged: Boolean,
    ) {
        _form.value = form.value.copy(
            termForms = SchoolYearFormProvider.sanitizeDates(
                form.value.termForms,
                changedDateIndex = changedIndex,
                isStartDateChanged = isStartDateChanged,
            )
        )
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val SCHOOL_YEAR_ID_KEY = SchoolYearNavigation.schoolYearIdArg
        private const val IS_DELETED_KEY = "is-deleted"
    }
}