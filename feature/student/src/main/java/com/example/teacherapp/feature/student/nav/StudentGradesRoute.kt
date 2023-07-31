package com.example.teacherapp.feature.student.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.feature.student.StudentGradesScreen
import com.example.teacherapp.feature.student.data.StudentGradesViewModel

@Composable
internal fun StudentGradesRoute(
    student: Student,
    viewModel: StudentGradesViewModel = hiltViewModel(),
) {
    val studentGradesResult by viewModel.studentGradesResult.collectAsStateWithLifecycle()
    val gradeDialog by viewModel.gradeDialog.collectAsStateWithLifecycle()

    StudentGradesScreen(
        studentGradesResult = studentGradesResult,
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