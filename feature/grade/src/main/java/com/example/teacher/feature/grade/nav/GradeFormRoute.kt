package com.example.teacher.feature.grade.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.grade.GradeFormScreen
import com.example.teacher.feature.grade.data.GradeFormViewModel

@Composable
internal fun GradeFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: (message: String) -> Unit,
    isEditMode: Boolean,
    viewModel: GradeFormViewModel = hiltViewModel(),
) {
    val uiStateResult by viewModel.uiState.collectAsStateWithLifecycle()
    val initialGrade by viewModel.initialGrade.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
    val form = viewModel.form
    val formStatus = form.status

    // Observe save.
    LaunchedEffect(formStatus, onShowSnackbar, onNavBack) {
        if (formStatus == FormStatus.Success) {
            onShowSnackbar("Zapisano ocenę")
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isDeleted, onShowSnackbar, onNavBack) {
        if (isDeleted) {
            onShowSnackbar("Usunięto ocenę")
            onNavBack()
        }
    }

    GradeFormScreen(
        uiStateResult = uiStateResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        formStatus = form.status,
        initialGrade = initialGrade,
        inputGrade = form.grade.value,
        onGradeChange = viewModel::onGradeChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onSubmit = viewModel::onSubmit,
        isEditMode = isEditMode,
        isDeleted = isDeleted,
        onDeleteClick = viewModel::onDelete,
    )
}