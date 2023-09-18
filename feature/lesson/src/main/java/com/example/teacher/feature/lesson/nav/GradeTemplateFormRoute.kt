package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.gradetemplate.GradeTemplateFormScreen
import com.example.teacher.feature.lesson.gradetemplate.data.GradeTemplateFormViewModel

@Composable
internal fun GradeTemplateFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onDelete: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    isEditMode: Boolean,
    viewModel: GradeTemplateFormViewModel = hiltViewModel(),
) {
    val gradeTemplateResult by viewModel.gradeTemplateResult.collectAsStateWithLifecycle()
    val lessonResult by viewModel.lessonResult.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
    val form = viewModel.form
    val formStatus = form.status

    // Observe save.
    LaunchedEffect(formStatus) {
        if (formStatus == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.lesson_grade_saved)
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.lesson_grade_deleted)
            onDelete()
        }
    }

    // TODO: Handle back press to prevent accidentally closing form.

    val lesson = remember(lessonResult) { (lessonResult as? Result.Success)?.data }

    GradeTemplateFormScreen(
        gradeTemplateResult = gradeTemplateResult,
        lesson = lesson,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        formStatus = form.status,
        name = form.name,
        onNameChange = viewModel::onNameChange,
        description = form.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        weight = form.weight,
        onWeightChange = viewModel::onWeightChange,
        isFirstTerm = form.isFirstTerm,
        onIsFirstTermChange = viewModel::onIsFirstTermChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddGrade = viewModel::onSubmit,
        isEditMode = isEditMode,
        isDeleted = isDeleted,
        onDeleteClick = viewModel::onDelete,
    )
}