package com.example.teacherapp.feature.schoolclass.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.feature.schoolclass.nav.SchoolClassNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SchoolClassViewModel @Inject constructor(
    private val repository: SchoolClassRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val schoolClassId = savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)
    val isSchoolClassDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassResult: StateFlow<Result<SchoolClass>> = schoolClassId
        .flatMapLatest { id -> repository.getSchoolClassById(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    val isSchoolYearExpanded = mutableStateOf(false)
    val isStudentsExpanded = mutableStateOf(false)
    val isLessonsExpanded = mutableStateOf(false)

    fun onDeleteSchoolClass() {
        viewModelScope.launch {
            repository.deleteSchoolClassById(schoolClassId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    companion object {
        private const val SCHOOL_CLASS_ID_KEY = SchoolClassNavigation.schoolClassIdArg
        private const val IS_DELETED_KEY = "is-deleted"
    }
}