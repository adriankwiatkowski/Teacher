package com.example.teacherapp.ui.screens.student.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.student.StudentRepository
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentScaffoldViewModel @Inject constructor(
    private val repository: StudentRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    val isStudentDeleted = savedStateHandle.getStateFlow(IS_STUDENT_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentResult: StateFlow<Result<Student>> = studentId
        .flatMapLatest { studentId -> repository.getStudentById(studentId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    fun onDeleteStudent() {
        if (isStudentDeleted.value) {
            return
        }

        viewModelScope.launch {
            repository.deleteStudentById(studentId.value)
            savedStateHandle[IS_STUDENT_DELETED_KEY] = true
        }
    }

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
        private const val IS_STUDENT_DELETED_KEY = "is-student-deleted"
    }
}