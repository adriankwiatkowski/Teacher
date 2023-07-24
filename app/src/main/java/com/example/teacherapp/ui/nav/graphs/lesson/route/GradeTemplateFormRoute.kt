package com.example.teacherapp.ui.nav.graphs.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.ui.screens.gradetemplate.GradeTemplateFormScreen
import com.example.teacherapp.ui.screens.gradetemplate.data.GradeTemplateFormViewModel

@Composable
internal fun GradeTemplateFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    isEditMode: Boolean,
    viewModel: GradeTemplateFormViewModel = hiltViewModel(),
) {
    val gradeTemplateResult by viewModel.gradeTemplateResult.collectAsStateWithLifecycle()
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

    GradeTemplateFormScreen(
        gradeTemplateResult = gradeTemplateResult,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        formStatus = form.status,
        name = form.name,
        onNameChange = viewModel::onNameChange,
        description = form.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        weight = form.weight,
        onWeightChange = viewModel::onWeightChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddGrade = viewModel::onSubmit,
        isEditMode = isEditMode,
        isDeleted = isDeleted,
        onDeleteClick = viewModel::onDelete,
    )
}