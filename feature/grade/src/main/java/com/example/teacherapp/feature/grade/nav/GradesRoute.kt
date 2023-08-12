package com.example.teacherapp.feature.grade.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.feature.grade.GradesScreen
import com.example.teacherapp.feature.grade.data.GradesViewModel

@Composable
internal fun GradesRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: (message: String) -> Unit,
    onEditClick: () -> Unit,
    onStudentClick: (studentId: Long, gradeId: Long?) -> Unit,
    viewModel: GradesViewModel = hiltViewModel(),
) {
    val uiStateResult by viewModel.uiState.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()

    // Observe deletion.
    LaunchedEffect(isDeleted, onShowSnackbar, onNavBack) {
        if (isDeleted) {
            onShowSnackbar("Usunięto ocenę")
            onNavBack()
        }
    }
    // TODO: GradeTemplate deletion should be observed from shared source like ViewModel.
    LaunchedEffect(uiStateResult, onNavBack) {
        if (uiStateResult is Result.Error) {
            onNavBack()
        }
    }

    GradesScreen(
        uiStateResult = uiStateResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        isDeleted = isDeleted,
        onDeleteClick = viewModel::onDelete,
        onEditClick = onEditClick,
        onStudentClick = onStudentClick,
    )
}