package com.example.teacher.feature.schoolclass.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    var form by mutableStateOf(SchoolClassFormProvider.createDefaultForm())
        private set

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
                        schoolYear.id == form.schoolYear.value?.id
                    } ?: schoolYears.firstOrNull()
                onSchoolYearChange(defaultSchoolYear)
            }
            .launchIn(viewModelScope)

        schoolClassResult
            .onEach { schoolClassResult ->
                val schoolClass = (schoolClassResult as? Result.Success)?.data
                if (schoolClass == null) {
                    form = SchoolClassFormProvider.createDefaultForm()
                    return@onEach
                }

                form = form.copy(
                    schoolClassName = SchoolClassFormProvider.validateSchoolClassName(schoolClass.name),
                    schoolYear = SchoolClassFormProvider.validateSchoolYear(schoolClass.schoolYear),
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
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
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateSchoolClass(
                id = schoolClassId.value,
                schoolYearId = form.schoolYear.value!!.id,
                name = form.schoolClassName.value,
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
        private const val SCHOOL_CLASS_ID_KEY = SchoolClassNavigation.schoolClassIdArg
    }
}