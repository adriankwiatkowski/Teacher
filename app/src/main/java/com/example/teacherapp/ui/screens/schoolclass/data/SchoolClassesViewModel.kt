package com.example.teacherapp.ui.screens.schoolclass.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.data.db.repository.SchoolClassRepository
import com.example.teacherapp.data.models.entities.BasicSchoolClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SchoolClassesViewModel @Inject constructor(
    repository: SchoolClassRepository,
) : ViewModel() {

    val schoolClassesResult: StateFlow<Result<List<BasicSchoolClass>>> = repository
        .getAllSchoolClasses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )
}