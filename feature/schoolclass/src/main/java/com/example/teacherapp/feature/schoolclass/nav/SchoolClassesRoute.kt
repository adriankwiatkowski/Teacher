package com.example.teacherapp.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.schoolclass.SchoolClassesScreen
import com.example.teacherapp.feature.schoolclass.data.SchoolClassesViewModel

@Composable
internal fun SchoolClassesRoute(
    snackbarHostState: SnackbarHostState,
    onAddSchoolClassClick: () -> Unit,
    onClassClick: (id: Long) -> Unit,
    onStudentsClick: (classId: Long) -> Unit,
    onLessonsClick: (classId: Long) -> Unit,
    viewModel: SchoolClassesViewModel = hiltViewModel(),
) {
    val schoolClassesResult by viewModel.schoolClassesResult.collectAsStateWithLifecycle()

    SchoolClassesScreen(
        schoolClassesResult = schoolClassesResult,
        snackbarHostState = snackbarHostState,
        onAddSchoolClassClick = onAddSchoolClassClick,
        onClassClick = onClassClick,
        onStudentsClick = onStudentsClick,
        onLessonsClick = onLessonsClick,
    )
}