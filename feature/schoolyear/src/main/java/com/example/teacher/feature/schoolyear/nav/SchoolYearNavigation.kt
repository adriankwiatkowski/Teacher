package com.example.teacher.feature.schoolyear.nav

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.schoolyear.nav.SchoolYearNavigation.schoolYearIdArg

private const val schoolYearFormScreen = "school-year-form"

object SchoolYearNavigation {
    internal const val schoolYearIdArg = "school-year-id"
}

private const val schoolYearFormRoute = "$schoolYearFormScreen?$schoolYearIdArg={$schoolYearIdArg}"

fun NavController.navigateToSchoolYearFormRoute(
    schoolYearId: Long? = null,
    navOptions: NavOptions? = null
) {
    val query = if (schoolYearId != null) "?$schoolYearIdArg=$schoolYearId" else ""
    this.navigate("$schoolYearFormScreen$query", navOptions)
}

fun NavGraphBuilder.schoolYearGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    onDelete: () -> Unit,
) {
    composable(
        schoolYearFormRoute,
        arguments = listOf(
            navArgument(schoolYearIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val isEditMode = args.getLong(schoolYearIdArg) != 0L

        SchoolYearFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onDelete = onDelete,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            isEditMode = isEditMode,
        )
    }
}