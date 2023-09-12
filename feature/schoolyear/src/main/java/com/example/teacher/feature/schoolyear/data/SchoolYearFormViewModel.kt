package com.example.teacher.feature.schoolyear.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolyear.SchoolYearRepository
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.schoolyear.nav.SchoolYearNavigation
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class SchoolYearFormViewModel @Inject constructor(
    private val repository: SchoolYearRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val schoolYearId = savedStateHandle.getStateFlow(SCHOOL_YEAR_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val schoolYearResult: StateFlow<Result<SchoolYear?>> = schoolYearId
        .flatMapLatest { schoolYearId -> repository.getSchoolYearById(schoolYearId) }
        .stateIn(initialValue = Result.Loading)

    var form by mutableStateOf(SchoolYearFormProvider.createDefaultForm())
        private set

    init {
        schoolYearResult
            .onEach { schoolYearResult ->
                val schoolYear = (schoolYearResult as? Result.Success)?.data
                if (schoolYear == null) {
                    form = SchoolYearFormProvider.createDefaultForm()
                    return@onEach
                }

                form = form.copy(
                    schoolYearName = SchoolYearFormProvider.validateSchoolYearName(schoolYear.name),
                    termForms = listOf(schoolYear.firstTerm, schoolYear.secondTerm).map { term ->
                        TermForm(
                            id = term.id,
                            name = SchoolYearFormProvider.validateTermName(term.name),
                            startDate = SchoolYearFormProvider.validateStartDate(term.startDate),
                            endDate = SchoolYearFormProvider.validateEndDate(term.endDate),
                        )
                    },
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onSchoolYearNameChange(name: String) {
        form = form.copy(schoolYearName = SchoolYearFormProvider.validateSchoolYearName(name))
    }

    fun onTermNameChange(index: Int, name: String) {
        val oldTerms = form.termForms
        val newTerms = oldTerms.toMutableList()
        newTerms[index] = newTerms[index].copy(name = SchoolYearFormProvider.validateTermName(name))

        form = form.copy(termForms = newTerms)
    }

    fun onStartDateChange(index: Int, date: LocalDate) {
        val oldTerms = form.termForms
        val newTerms = oldTerms.toMutableList()
        newTerms[index] =
            newTerms[index].copy(startDate = SchoolYearFormProvider.validateStartDate(date))

        form = form.copy(termForms = newTerms)
        sanitizeDates(changedIndex = index, isStartDateChanged = true)
    }

    fun onEndDateChange(index: Int, date: LocalDate) {
        val oldTerms = form.termForms
        val newTerms = oldTerms.toMutableList()
        newTerms[index] =
            newTerms[index].copy(endDate = SchoolYearFormProvider.validateEndDate(date))

        form = form.copy(termForms = newTerms)
        sanitizeDates(changedIndex = index, isStartDateChanged = false)
    }

    fun onAddSchoolYear() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            val firstTerm = form.termForms[0]
            val secondTerm = form.termForms[1]
            val schoolYearName = form.schoolYearName.value

            repository.insertOrUpdateSchoolYear(
                id = schoolYearId.value.let { id -> if (id != 0L) id else null },
                schoolYearName = schoolYearName,
                termFirstId = firstTerm.id,
                termFirstName = firstTerm.name.value,
                termFirstStartDate = firstTerm.startDate.date,
                termFirstEndDate = firstTerm.endDate.date,
                termSecondId = secondTerm.id,
                termSecondName = secondTerm.name.value,
                termSecondStartDate = secondTerm.startDate.date,
                termSecondEndDate = secondTerm.endDate.date,
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
            }
        }
    }

    private fun sanitizeDates(
        changedIndex: Int,
        isStartDateChanged: Boolean,
    ) {
        form = form.copy(
            termForms = SchoolYearFormProvider.sanitizeDates(
                form.termForms,
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
    }
}