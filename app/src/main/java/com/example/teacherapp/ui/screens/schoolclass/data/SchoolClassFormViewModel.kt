package com.example.teacherapp.ui.screens.schoolclass.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.repository.SchoolClassRepository
import com.example.teacherapp.data.models.entities.SchoolYear
import com.example.teacherapp.data.models.input.FormStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolClassFormViewModel @Inject constructor(
    private val repository: SchoolClassRepository,
) : ViewModel() {

    var form by mutableStateOf(SchoolClassFormProvider.createDefaultForm())
        private set

    val schoolYears = repository.getAllSchoolYears()
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
        if (!form.canSubmit) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertSchoolClass(
                schoolYearId = schoolYear.value!!.id,
                name = schoolClassName.value,
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
            }
        }
    }
}