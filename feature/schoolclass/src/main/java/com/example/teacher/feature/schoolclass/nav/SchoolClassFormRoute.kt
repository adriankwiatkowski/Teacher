package com.example.teacher.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.schoolclass.R
import com.example.teacher.feature.schoolclass.SchoolClassFormScreen
import com.example.teacher.feature.schoolclass.data.SchoolClassFormViewModel

@Composable
internal fun SchoolClassFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    onAddSchoolYear: () -> Unit,
) {
    val viewModel = hiltViewModel<SchoolClassFormViewModel>()
    val schoolYears by viewModel.schoolYears.collectAsStateWithLifecycle()
    val form = viewModel.form
    val status = form.status

    LaunchedEffect(status, onShowSnackbar, onNavBack) {
        if (status == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.school_class_saved)
            onNavBack()
        }
    }

    SchoolClassFormScreen(
        snackbarHostState = snackbarHostState,
        schoolClassName = form.schoolClassName,
        onSchoolClassNameChange = viewModel::onSchoolClassNameChange,
        schoolYears = schoolYears,
        schoolYear = form.schoolYear,
        onSchoolYearChange = viewModel::onSchoolYearChange,
        formStatus = form.status,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddSchoolYear = onAddSchoolYear,
        onAddSchoolClass = viewModel::onSubmit,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
    )
}