package com.example.teacherapp.ui.screens.schoolclass.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolClassViewModel @Inject constructor(
    private val repository: SchoolClassRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val schoolClassId = savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)

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

    var isSchoolClassDeleted by mutableStateOf(false)

    fun deleteSchoolClass() {
        viewModelScope.launch {
            isSchoolClassDeleted = false
            repository.deleteSchoolClassById(schoolClassId.value)
            isSchoolClassDeleted = true
        }
    }

    companion object {
        private const val SCHOOL_CLASS_ID_KEY = TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG
    }
}