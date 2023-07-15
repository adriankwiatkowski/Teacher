package com.example.teacherapp.ui.nav.graphs.schoolclass.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassesScreen
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassesViewModel

@Composable
internal fun SchoolClassesRoute(
    onAddSchoolClassClick: () -> Unit,
    onClassClick: (id: Long) -> Unit,
    onStudentsClick: (classId: Long) -> Unit,
    onLessonsClick: (classId: Long) -> Unit,
    viewModel: SchoolClassesViewModel = hiltViewModel(),
) {
    val schoolClassesResult by viewModel.schoolClassesResult.collectAsStateWithLifecycle()

    SchoolClassesScreen(
        schoolClassesResult = schoolClassesResult,
        onAddSchoolClassClick = onAddSchoolClassClick,
        onClassClick = onClassClick,
        onStudentsClick = onStudentsClick,
        onLessonsClick = onLessonsClick,
    )
}