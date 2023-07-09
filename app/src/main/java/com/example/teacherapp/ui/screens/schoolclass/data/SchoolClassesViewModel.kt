package com.example.teacherapp.ui.screens.schoolclass.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.schoolclass.SchoolClassDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SchoolClassesViewModel @Inject constructor(
    schoolClassDataSource: SchoolClassDataSource,
) : ViewModel() {

    val schoolClasses = schoolClassDataSource.getAllSchoolClasses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}