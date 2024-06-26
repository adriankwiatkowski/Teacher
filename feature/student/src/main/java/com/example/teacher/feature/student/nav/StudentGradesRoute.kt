package com.example.teacher.feature.student.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.model.data.Student
import com.example.teacher.feature.student.StudentGradesScreen
import com.example.teacher.feature.student.data.StudentGradesViewModel

@Composable
internal fun StudentGradesRoute(
    snackbarHostState: SnackbarHostState,
    student: Student,
    viewModel: StudentGradesViewModel = hiltViewModel(),
) {
    val studentGradesResult by viewModel.studentGradesResult.collectAsStateWithLifecycle()
    val gradeDialog by viewModel.gradeDialog.collectAsStateWithLifecycle()

    StudentGradesScreen(
        studentGradesResult = studentGradesResult,
        snackbarHostState = snackbarHostState,
        gradeDialog = gradeDialog,
        onShowGradeDialog = { gradeInfo, grade ->
            viewModel.onShowGradeDialog(
                student = student,
                gradeInfo = gradeInfo,
                grade = grade,
            )
        },
        onGradeDialogDismiss = viewModel::onDismissGradeDialog,
    )
}