package com.example.teacher.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.feature.schoolclass.SchoolClassStudentsScreen
import com.example.teacher.feature.schoolclass.data.SchoolClassStudentsService

@Composable
internal fun SchoolClassStudentsRoute(
    snackbarHostState: SnackbarHostState,
    onStudentClick: (studentId: Long) -> Unit,
    onAddStudentClick: () -> Unit,
    students: List<BasicStudent>,
    service: SchoolClassStudentsService,
) {
    val randomStudent by service.randomStudent.collectAsStateWithLifecycle()

    SchoolClassStudentsScreen(
        snackbarHostState = snackbarHostState,
        students = students,
        randomStudent = randomStudent,
        pickRandomStudent = service::pickRandomStudent,
        onStudentClick = onStudentClick,
        onAddStudentClick = onAddStudentClick,
    )
}