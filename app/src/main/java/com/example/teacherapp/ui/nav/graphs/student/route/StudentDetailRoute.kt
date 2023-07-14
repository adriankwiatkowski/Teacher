package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.ui.screens.student.StudentDetailScreen

@Composable
internal fun StudentDetailRoute(
    student: Student,
    modifier: Modifier = Modifier,
) {
    val isGradesExpanded = remember { mutableStateOf(true) }

    StudentDetailScreen(
        modifier = modifier,
        student = student,
        onEmailClick = {},
        onPhoneClick = {},
        onGradeClick = {},
        onAddGradeClick = {},
        isGradesExpanded = isGradesExpanded,
    )
}