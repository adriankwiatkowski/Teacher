package com.example.teacherapp.feature.student.route

import androidx.compose.runtime.Composable
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.feature.student.StudentDetailScreen

@Composable
internal fun StudentDetailRoute(student: Student) {
    StudentDetailScreen(
        student = student,
        onEmailClick = {},
        onPhoneClick = {},
    )
}