package com.example.teacherapp.ui.nav.graphs.schoolyear

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.ui.screens.schoolyear.SchoolYearFormScreen
import com.example.teacherapp.ui.screens.schoolyear.data.SchoolYearFormViewModel

private const val schoolYearFormScreen = "school-year-form"
private const val schoolYearFormRoute = schoolYearFormScreen

fun NavController.navigateToSchoolYearFormRoute(navOptions: NavOptions? = null) {
    this.navigate(schoolYearFormScreen, navOptions)
}

fun NavGraphBuilder.schoolYearGraph(navController: NavController) {
    composable(schoolYearFormRoute) {
        val viewModel = hiltViewModel<SchoolYearFormViewModel>()
        val form = viewModel.form

        SchoolYearFormScreen(
            termForms = form.termForms,
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            schoolYearName = form.schoolYearName,
            onSchoolYearNameChange = viewModel::onSchoolYearNameChange,
            onTermNameChange = viewModel::onTermNameChange,
            onStartDateChange = viewModel::onStartDateChange,
            onEndDateChange = viewModel::onEndDateChange,
            status = form.status,
            isSubmitEnabled = form.isSubmitEnabled,
            onAddSchoolYear = viewModel::onAddSchoolYear,
            onSchoolYearAdded = navController::popBackStack,
        )
    }
}