package com.example.teacherapp.feature.schoolclass.nav

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacherapp.feature.schoolclass.data.SchoolClassesViewModel
import com.example.teacherapp.feature.schoolclass.nav.SchoolClassNavigation.schoolClassGraphRoute
import com.example.teacherapp.feature.schoolclass.nav.SchoolClassNavigation.schoolClassIdArg
import com.example.teacherapp.feature.schoolclass.nav.SchoolClassNavigation.schoolClassRoute
import com.example.teacherapp.feature.schoolclass.nav.SchoolClassNavigation.schoolClassesRoute

private const val schoolClassesScreen = "school-classes"
private const val schoolClassScreen = "school-class"
private const val schoolClassFormScreen = "school-class-form"

object SchoolClassNavigation {
    const val schoolClassGraphRoute = "school-class"

    internal const val schoolClassIdArg = "school-class-id"

    const val schoolClassesRoute = schoolClassesScreen
    const val schoolClassRoute = "$schoolClassScreen/{$schoolClassIdArg}"
}

private const val schoolClassFormRoute = schoolClassFormScreen

fun NavController.navigateToSchoolClassGraph(navOptions: NavOptions? = null) {
    this.navigate(schoolClassGraphRoute, navOptions)
}

private fun NavController.navigateToSchoolClassRoute(
    schoolClassId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$schoolClassScreen/$schoolClassId", navOptions)
}

private fun NavController.navigateToSchoolClassFormRoute(navOptions: NavOptions? = null) {
    this.navigate(schoolClassFormScreen, navOptions)
}

fun NavGraphBuilder.schoolClassGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
    navigateToSchoolYearForm: () -> Unit,
    navigateToStudentGraph: (schoolClassId: Long, studentId: Long) -> Unit,
    navigateToStudentFormRoute: (schoolClassId: Long, studentId: Long?) -> Unit,
    navigateToLessonGraph: (schoolClassId: Long, lessonId: Long) -> Unit,
    navigateToLessonFormRoute: (schoolClassId: Long, lessonId: Long?) -> Unit,
) {
    navigation(
        startDestination = schoolClassesRoute,
        route = schoolClassGraphRoute
    ) {
        composable(schoolClassesRoute) {
            val viewModel = hiltViewModel<SchoolClassesViewModel>()

            SchoolClassesRoute(
                onAddSchoolClassClick = navController::navigateToSchoolClassFormRoute,
                onClassClick = { schoolClassId ->
                    navController.navigateToSchoolClassRoute(schoolClassId = schoolClassId)
                },
                onStudentsClick = { schoolClassId ->
                },
                onLessonsClick = { schoolClassId ->
                },
                viewModel = viewModel,
            )
        }

        composable(
            schoolClassRoute,
            arguments = listOf(
                navArgument(schoolClassIdArg) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)

            SchoolClassRoute(
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                onStudentClick = { studentId ->
                    navigateToStudentGraph(schoolClassId, studentId)
                },
                onAddStudentClick = {
                    navigateToStudentFormRoute(schoolClassId, null)
                },
                onLessonClick = { lessonId ->
                    navigateToLessonGraph(schoolClassId, lessonId)
                },
                onAddLessonClick = {
                    navigateToLessonFormRoute(schoolClassId, null)
                },
            )
        }
    }

    composable(schoolClassFormRoute) {
        SchoolClassFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            onAddSchoolYear = navigateToSchoolYearForm,
        )
    }
}