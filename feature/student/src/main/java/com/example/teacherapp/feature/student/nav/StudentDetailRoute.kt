package com.example.teacherapp.feature.student.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.feature.student.StudentDetailScreen

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