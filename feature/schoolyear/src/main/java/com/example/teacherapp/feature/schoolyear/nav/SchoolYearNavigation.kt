package com.example.teacherapp.feature.schoolyear.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

private const val schoolYearFormScreen = "school-year-form"
private const val schoolYearFormRoute = schoolYearFormScreen

fun NavController.navigateToSchoolYearFormRoute(navOptions: NavOptions? = null) {
    this.navigate(schoolYearFormScreen, navOptions)
}

fun NavGraphBuilder.schoolYearGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    composable(schoolYearFormRoute) {
        SchoolYearFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
        )
    }
}