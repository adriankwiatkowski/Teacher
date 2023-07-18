package com.example.teacherapp.ui.screens.student.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.student.StudentRepository
import com.example.teacherapp.core.model.data.StudentGradesByLesson
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StudentGradesViewModel @Inject constructor(
    private val repository: StudentRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentGradesResult: StateFlow<Result<List<StudentGradesByLesson>>> = studentId
        .flatMapLatest { studentId -> repository.getStudentGradesById(studentId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
    }
}