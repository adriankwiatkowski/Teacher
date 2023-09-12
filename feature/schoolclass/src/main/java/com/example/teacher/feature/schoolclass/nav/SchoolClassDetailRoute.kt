package com.example.teacher.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.feature.schoolclass.SchoolClassDetailScreen

@Composable
internal fun SchoolClassDetailRoute(
    snackbarHostState: SnackbarHostState,
    schoolYear: SchoolYear,
) {
    SchoolClassDetailScreen(
        snackbarHostState = snackbarHostState,
        schoolYear = schoolYear,
    )
}