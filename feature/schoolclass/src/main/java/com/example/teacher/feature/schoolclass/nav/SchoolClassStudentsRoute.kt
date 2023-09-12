package com.example.teacher.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.feature.schoolclass.SchoolClassStudentsScreen

@Composable
internal fun SchoolClassStudentsRoute(
    snackbarHostState: SnackbarHostState,
    onStudentClick: (studentId: Long) -> Unit,
    onAddStudentClick: () -> Unit,
    students: List<BasicStudent>,
) {
    SchoolClassStudentsScreen(
        snackbarHostState = snackbarHostState,
        students = students,
        onStudentClick = onStudentClick,
        onAddStudentClick = onAddStudentClick,
    )
}