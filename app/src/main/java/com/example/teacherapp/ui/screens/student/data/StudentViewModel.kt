package com.example.teacherapp.ui.screens.student.data

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.student.StudentDataSource
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.ResourceStatus
import com.example.teacherapp.data.models.entities.Student
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentDataSource: StudentDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val resourceStatus = MutableStateFlow<ResourceStatus>(ResourceStatus.Loading)

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val student: Flow<Student?> = studentId
        .flatMapLatest { id ->
            resourceStatus.value = ResourceStatus.Loading
            studentDataSource.getStudentById(id).also {
                resourceStatus.value = ResourceStatus.Success
            }
        }
        .catch { e ->
            if (e !is SQLiteException) {
                throw e
            }

            resourceStatus.value = ResourceStatus.Error
        }

    val uiState: StateFlow<Resource<Student>> =
        combine(resourceStatus, student) { status, student ->
            when (status) {
                ResourceStatus.Loading -> Resource.Loading
                ResourceStatus.Error -> Resource.Error(NoSuchElementException())
                ResourceStatus.Success -> {
                    if (student != null) {
                        Resource.Success(student)
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

    val isGradesExpanded = mutableStateOf(false)
    val isNotesExpanded = mutableStateOf(false)

    var isStudentDeleted by mutableStateOf(false)

    fun deleteStudent() {
        viewModelScope.launch {
            isStudentDeleted = false

            studentDataSource.deleteStudentById(studentId.value)

            isStudentDeleted = true
        }
    }

    companion object {
        private const val STUDENT_ID_KEY = TeacherDestinationsArgs.STUDENT_ID_ARG
    }
}