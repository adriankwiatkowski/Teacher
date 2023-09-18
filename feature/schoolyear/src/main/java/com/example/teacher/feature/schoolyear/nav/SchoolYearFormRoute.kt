package com.example.teacher.feature.schoolyear.nav

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val form = viewModel.form
    val status = form.status

    // Observe save.
    LaunchedEffect(status) {
        if (status == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.school_year_saved)
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

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    BackPressDiscardDialogHandler(
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
        onDeleteSchoolYear = viewModel::onDeleteSchoolYear,
    )
}