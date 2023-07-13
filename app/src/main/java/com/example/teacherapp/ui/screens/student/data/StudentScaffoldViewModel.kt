package com.example.teacherapp.ui.screens.student.data

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.student.StudentDataSource
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.ResourceStatus
import com.example.teacherapp.data.models.entities.Student
import com.example.teacherapp.ui.nav.graphs.student.studentIdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentScaffoldViewModel @Inject constructor(
    private val studentDataSource: StudentDataSource,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val resourceStatus = MutableStateFlow<ResourceStatus>(ResourceStatus.Loading)

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    val isStudentDeleted = savedStateHandle.getStateFlow(IS_STUDENT_DELETED_KEY, false)

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

    val studentResource: StateFlow<Resource<Student>> =
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

    fun onDeleteStudent() {
        if (isStudentDeleted.value) {
            return
        }

        viewModelScope.launch {
            studentDataSource.deleteStudentById(studentId.value)
            savedStateHandle[IS_STUDENT_DELETED_KEY] = true
        }
    }

    companion object {
        private const val STUDENT_ID_KEY = studentIdArg
        private const val IS_STUDENT_DELETED_KEY = "is-student-deleted"
    }
}