package com.example.teacher.feature.schoolclass.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacher.core.model.data.BasicSchoolClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class SchoolClassesViewModel @Inject constructor(
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