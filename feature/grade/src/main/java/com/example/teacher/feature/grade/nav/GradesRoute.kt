package com.example.teacher.feature.grade.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.component.TeacherDeleteDialog
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.grade.GradesScreen
import com.example.teacher.feature.grade.R
import com.example.teacher.feature.grade.data.GradesViewModel

@Composable
internal fun GradesRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    onEditClick: () -> Unit,
    onStudentClick: (studentId: Long, gradeId: Long?) -> Unit,
    viewModel: GradesViewModel = hiltViewModel(),
) {
    val uiStateResult by viewModel.uiState.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()

    // Observe deletion.
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.grade_grade_deleted)
            onNavBack()
        }
    }

    // Handle delete dialog confirmation.
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    if (showDeleteDialog) {
        TeacherDeleteDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmClick = {
                viewModel.onDelete()
                showDeleteDialog = false
            },
        )
    }

    GradesScreen(
        uiStateResult = uiStateResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        isDeleted = isDeleted,
        onDeleteClick = { showDeleteDialog = true },
        onEditClick = onEditClick,
        onStudentClick = onStudentClick,
    )
}