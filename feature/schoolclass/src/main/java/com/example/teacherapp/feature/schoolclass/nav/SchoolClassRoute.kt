package com.example.teacherapp.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.schoolclass.SchoolClassScreen
import com.example.teacherapp.feature.schoolclass.data.SchoolClassViewModel

@Composable
internal fun SchoolClassRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: (message: String) -> Unit,
    onStudentClick: (id: Long) -> Unit,
    onAddStudentClick: () -> Unit,
    onLessonClick: (id: Long) -> Unit,
    onAddLessonClick: () -> Unit,
    viewModel: SchoolClassViewModel = hiltViewModel(),
) {
    val schoolClassResult by viewModel.schoolClassResult.collectAsStateWithLifecycle()
    val isSchoolClassDeleted by viewModel.isSchoolClassDeleted.collectAsStateWithLifecycle()

    // Observe deletion.
    LaunchedEffect(isSchoolClassDeleted, onShowSnackbar, onNavBack) {
        if (isSchoolClassDeleted) {
            onShowSnackbar("Usunięto klasę")
            onNavBack()
        }
    }

    SchoolClassScreen(
        schoolClassResult = schoolClassResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        onStudentClick = onStudentClick,
        onAddStudentClick = onAddStudentClick,
        onLessonClick = onLessonClick,
        onAddLessonClick = onAddLessonClick,
        isSchoolYearExpanded = viewModel.isSchoolYearExpanded,
        isStudentsExpanded = viewModel.isStudentsExpanded,
        isLessonsExpanded = viewModel.isLessonsExpanded,
        isSchoolClassDeleted = isSchoolClassDeleted,
        onDeleteSchoolClassClick = viewModel::onDeleteSchoolClass,
    )
}