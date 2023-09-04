package com.example.teacher.feature.student.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.teacher.core.model.data.Student
import com.example.teacher.feature.student.StudentDetailScreen

@Composable
internal fun StudentDetailRoute(
    snackbarHostState: SnackbarHostState,
    student: Student,
) {
    StudentDetailScreen(
        snackbarHostState = snackbarHostState,
        student = student,
        onEmailClick = {},
        onPhoneClick = {},
    )
}