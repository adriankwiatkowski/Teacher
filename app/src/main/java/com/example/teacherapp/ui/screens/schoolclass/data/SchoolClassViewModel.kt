package com.example.teacherapp.ui.screens.schoolclass.data

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.schoolclass.SchoolClassDataSource
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.ResourceStatus
import com.example.teacherapp.data.models.entities.SchoolClass
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SchoolClassViewModel @Inject constructor(
    schoolClassDataSource: SchoolClassDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val resourceStatus = MutableStateFlow<ResourceStatus>(ResourceStatus.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val schoolClass: Flow<SchoolClass?> = savedStateHandle
        .getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)
        .flatMapLatest { id ->
            resourceStatus.value = ResourceStatus.Loading
            schoolClassDataSource.getSchoolClassById(id).also {
                resourceStatus.value = ResourceStatus.Success
            }
        }
        .catch { e ->
            if (e !is SQLiteException) {
                throw e
            }

            resourceStatus.value = ResourceStatus.Error
        }

    val uiState: StateFlow<Resource<SchoolClass>> =
        combine(resourceStatus, schoolClass) { status, schoolClass ->
            when (status) {
                ResourceStatus.Loading -> Resource.Loading
                ResourceStatus.Error -> Resource.Error(NoSuchElementException())
                ResourceStatus.Success -> {
                    if (schoolClass != null) {
                        Resource.Success(schoolClass)
                    } else {
                        Resource.Error(NoSuchElementException())
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Resource.Loading,
        )

    val isSchoolYearExpanded = mutableStateOf(false)
    val isStudentsExpanded = mutableStateOf(false)
    val isLessonsExpanded = mutableStateOf(false)

    companion object {
        private const val SCHOOL_CLASS_ID_KEY = TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG
    }
}