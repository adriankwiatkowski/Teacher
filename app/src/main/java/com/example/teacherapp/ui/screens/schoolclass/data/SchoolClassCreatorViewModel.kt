package com.example.teacherapp.ui.screens.schoolclass.data

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.schoolclass.SchoolClassDataSource
import com.example.teacherapp.data.db.datasources.schoolyear.SchoolYearDataSource
import com.example.teacherapp.data.models.entities.SchoolYear
import com.example.teacherapp.data.models.input.FormStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolClassCreatorViewModel @Inject constructor(
    schoolYearDataSource: SchoolYearDataSource,
    private val schoolClassDataSource: SchoolClassDataSource,
) : ViewModel() {

    var form by mutableStateOf(SchoolClassFormProvider.createDefaultForm())
        private set

    val schoolYears = schoolYearDataSource.getAllSchoolYears()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList(),
        )

    val schoolClassName get() = form.schoolClassName
    val schoolYear get() = form.schoolYear
    val isValid get() = form.isValid
    val status get() = form.status

    init {
        schoolYears
            .onEach { schoolYears ->
                val defaultSchoolYear = schoolYears.firstOrNull() ?: return@onEach
                onSchoolYearChange(defaultSchoolYear)
            }
            .launchIn(viewModelScope)
    }

    fun onSchoolClassNameChange(name: String) {
        form = form.copy(schoolClassName = SchoolClassFormProvider.validateSchoolClassName(name))
    }

    fun onSchoolYearChange(schoolYear: SchoolYear?) {
        form = form.copy(schoolYear = SchoolClassFormProvider.validateSchoolYear(schoolYear))
    }

    fun onSubmit() {
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

            val schoolClassName = form.schoolClassName
            val schoolYear = form.schoolYear

            form = form.copy(status = FormStatus.Saving)

            schoolClassDataSource.insertSchoolClass(
                schoolYearId = schoolYear.value!!.id,
                name = schoolClassName.value,
            )

            form = form.copy(status = FormStatus.Success)
        }
    }
}