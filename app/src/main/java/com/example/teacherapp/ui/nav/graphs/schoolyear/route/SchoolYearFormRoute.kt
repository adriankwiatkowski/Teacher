package com.example.teacherapp.ui.nav.graphs.schoolyear.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.ui.screens.schoolyear.SchoolYearFormScreen
import com.example.teacherapp.ui.screens.schoolyear.data.SchoolYearFormViewModel

@Composable
internal fun SchoolYearFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
) {
    val viewModel = hiltViewModel<SchoolYearFormViewModel>()
    val form = viewModel.form
    val status = form.status

    LaunchedEffect(status, onShowSnackbar, onNavBack) {
        if (status == FormStatus.Success) {
            onShowSnackbar("Zapisano rok szkolny")
            onNavBack()
        }
    }

    SchoolYearFormScreen(
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