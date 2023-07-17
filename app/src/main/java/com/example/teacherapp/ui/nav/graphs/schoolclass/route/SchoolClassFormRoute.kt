package com.example.teacherapp.ui.nav.graphs.schoolclass.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassFormScreen
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassFormViewModel

@Composable
internal fun SchoolClassFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onAddSchoolYear: () -> Unit,
) {
    val viewModel = hiltViewModel<SchoolClassFormViewModel>()
    val schoolYears by viewModel.schoolYears.collectAsStateWithLifecycle()
    val form = viewModel.form
    val status = form.status

    LaunchedEffect(status, onNavBack) {
        if (status == FormStatus.Success) {
            onNavBack()
        }
    }

    SchoolClassFormScreen(
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