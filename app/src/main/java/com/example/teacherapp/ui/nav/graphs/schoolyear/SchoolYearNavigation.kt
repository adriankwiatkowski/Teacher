package com.example.teacherapp.ui.nav.graphs.schoolyear

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.ui.nav.graphs.schoolyear.route.SchoolYearFormRoute

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