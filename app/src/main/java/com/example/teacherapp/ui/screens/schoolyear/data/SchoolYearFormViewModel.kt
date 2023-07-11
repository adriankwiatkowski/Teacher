package com.example.teacherapp.ui.screens.schoolyear.data

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.schoolyear.SchoolYearDataSource
import com.example.teacherapp.data.models.input.FormStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SchoolYearFormViewModel @Inject constructor(
    private val schoolYearDataSource: SchoolYearDataSource,
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
        if (!form.isValid) {
            return
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            form = form.copy(status = FormStatus.Error)
            if (exception !is SQLiteConstraintException) {
                throw exception
            }
        }
        viewModelScope.launch(handler) {
            if (form.status == FormStatus.Saving || form.status == FormStatus.Success) {
                return@launch
            }

            val firstTerm = form.termForms[0]
            val secondTerm = form.termForms[1]
            val schoolYearName = form.schoolYearName.value

            form = form.copy(status = FormStatus.Saving)

            schoolYearDataSource.insertSchoolYear(
                schoolYearName = schoolYearName,
                termFirstName = firstTerm.name.value,
                termFirstStartDate = firstTerm.startDate.date,
                termFirstEndDate = firstTerm.endDate.date,
                termSecondName = secondTerm.name.value,
                termSecondStartDate = secondTerm.startDate.date,
                termSecondEndDate = secondTerm.endDate.date,
            )

            form = form.copy(status = FormStatus.Success)
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