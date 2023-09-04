package com.example.teacher.feature.schoolyear.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.data.repository.schoolyear.SchoolYearRepository
import com.example.teacher.core.ui.model.FormStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class SchoolYearFormViewModel @Inject constructor(
    private val repository: SchoolYearRepository,
) : ViewModel() {

    var form by mutableStateOf(SchoolYearFormProvider.createDefaultForm())
        private set

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

            repository.insertSchoolYear(
                schoolYearName = schoolYearName,
                termFirstName = firstTerm.name.value,
                termFirstStartDate = firstTerm.startDate.date,
                termFirstEndDate = firstTerm.endDate.date,
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
}