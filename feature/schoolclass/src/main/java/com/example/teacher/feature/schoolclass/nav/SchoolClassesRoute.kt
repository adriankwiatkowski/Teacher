package com.example.teacher.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.feature.schoolclass.SchoolClassesScreen
import com.example.teacher.feature.schoolclass.data.SchoolClassesViewModel

@Composable
internal fun SchoolClassesRoute(
    snackbarHostState: SnackbarHostState,
    onAddSchoolClassClick: () -> Unit,
    onClassClick: (id: Long) -> Unit,
    viewModel: SchoolClassesViewModel = hiltViewModel(),
) {
    val schoolClassesResult by viewModel.schoolClassesResult.collectAsStateWithLifecycle()

    SchoolClassesScreen(
        schoolClassesResult = schoolClassesResult,
        snackbarHostState = snackbarHostState,
        onAddSchoolClassClick = onAddSchoolClassClick,
        onClassClick = onClassClick,
    )
}