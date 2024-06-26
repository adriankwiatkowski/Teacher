package com.example.teacher.feature.schoolyear.nav

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.BackPressDiscardDialogHandler
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.schoolyear.R
import com.example.teacher.feature.schoolyear.SchoolYearFormScreen
import com.example.teacher.feature.schoolyear.data.SchoolYearFormViewModel

@Composable
internal fun SchoolYearFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onDelete: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    isEditMode: Boolean,
    viewModel: SchoolYearFormViewModel = hiltViewModel(),
) {
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()
    val isFormMutated by viewModel.isFormMutated.collectAsStateWithLifecycle()
    val status = form.status

    // Observe save.
    LaunchedEffect(status) {
        if (status == FormStatus.Success) {
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.school_year_deleted)
            onDelete()
            onNavBack()
        }
    }

    // Handle delete dialog confirmation.
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    if (showDeleteDialog) {
        TeacherDeleteDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmClick = {
                viewModel.onDeleteSchoolYear()
                showDeleteDialog = false
            },
        )
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    BackPressDiscardDialogHandler(
        enabled = isFormMutated,
        backPressedDispatcher = backPressedDispatcher,
        onDiscard = onNavBack,
    )

    SchoolYearFormScreen(
        snackbarHostState = snackbarHostState,
        termForms = form.termForms,
        showNavigationIcon = showNavigationIcon,
        onNavBack = { backPressedDispatcher?.onBackPressed() },
        isEditMode = isEditMode,
        isDeleted = isDeleted,
        schoolYearName = form.schoolYearName,
        onSchoolYearNameChange = viewModel::onSchoolYearNameChange,
        onTermNameChange = viewModel::onTermNameChange,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        status = form.status,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddSchoolYear = viewModel::onAddSchoolYear,
        onDeleteSchoolYear = { showDeleteDialog = true },
    )
}