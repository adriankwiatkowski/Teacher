package com.example.teacherapp.ui.screens.student.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.student.StudentRepository
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.core.model.data.StudentGrade
import com.example.teacherapp.core.model.data.StudentGradesByLesson
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        .stateIn(initialValue = Result.Loading)

    private val _gradeDialog = MutableStateFlow<GradeDialogInfo?>(null)
    val gradeDialog = _gradeDialog.asStateFlow()

    fun onShowGradeDialog(student: Student, gradeInfo: StudentGradesByLesson, grade: StudentGrade) {
        _gradeDialog.value = GradeDialogInfo(
            student = student,
            gradeInfo = gradeInfo,
            grade = grade,
        )
    }

    fun onDismissGradeDialog() {
        _gradeDialog.value = null
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
    }
}

data class GradeDialogInfo(
    val student: Student,
    val gradeInfo: StudentGradesByLesson,
    val grade: StudentGrade,
)