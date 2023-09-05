package com.example.teacher.feature.schoolyear.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.schoolyear.R
import com.example.teacher.feature.schoolyear.SchoolYearFormScreen
import com.example.teacher.feature.schoolyear.data.SchoolYearFormViewModel

@Composable
internal fun SchoolYearFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
) {
    val viewModel = hiltViewModel<SchoolYearFormViewModel>()
    val form = viewModel.form
    val status = form.status

    LaunchedEffect(status, onShowSnackbar, onNavBack) {
        if (status == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.school_year_saved)
            onNavBack()
        }
    }

    SchoolYearFormScreen(
        snackbarHostState = snackbarHostState,
        termForms = form.termForms,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        schoolYearName = form.schoolYearName,
        onSchoolYearNameChange = viewModel::onSchoolYearNameChange,
        onTermNameChange = viewModel::onTermNameChange,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        status = form.status,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddSchoolYear = viewModel::onAddSchoolYear,
    )
}