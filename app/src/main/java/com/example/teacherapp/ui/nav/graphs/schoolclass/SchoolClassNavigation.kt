package com.example.teacherapp.ui.nav.graphs.schoolclass

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacherapp.ui.nav.graphs.lesson.navigateToLessonFormRoute
import com.example.teacherapp.ui.nav.graphs.lesson.navigateToLessonGraph
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation.schoolClassGraphRoute
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation.schoolClassIdArg
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation.schoolClassRoute
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation.schoolClassesRoute
import com.example.teacherapp.ui.nav.graphs.schoolclass.route.SchoolClassFormRoute
import com.example.teacherapp.ui.nav.graphs.schoolclass.route.SchoolClassRoute
import com.example.teacherapp.ui.nav.graphs.schoolclass.route.SchoolClassesRoute
import com.example.teacherapp.ui.nav.graphs.schoolyear.navigateToSchoolYearFormRoute
import com.example.teacherapp.ui.nav.graphs.student.navigateToStudentFormRoute
import com.example.teacherapp.ui.nav.graphs.student.navigateToStudentGraph
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassesViewModel

private const val schoolClassesScreen = "school-classes"
private const val schoolClassScreen = "school-class"
private const val schoolClassFormScreen = "school-class-form"

internal object SchoolClassNavigation {
    internal const val schoolClassGraphRoute = "school-class"

    internal const val schoolClassIdArg = "school-class-id"

    internal const val schoolClassesRoute = schoolClassesScreen
    internal const val schoolClassRoute = "$schoolClassScreen/{$schoolClassIdArg}"
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
                    navController.navigateToStudentGraph(
                        schoolClassId = schoolClassId,
                        studentId = studentId,
                    )
                },
                onAddStudentClick = {
                    navController.navigateToStudentFormRoute(
                        schoolClassId = schoolClassId,
                        studentId = null,
                    )
                },
                onLessonClick = { lessonId ->
                    navController.navigateToLessonGraph(
                        schoolClassId = schoolClassId,
                        lessonId = lessonId,
                    )
                },
                onAddLessonClick = {
                    navController.navigateToLessonFormRoute(
                        schoolClassId = schoolClassId,
                        lessonId = null,
                    )
                },
            )
        }
    }

    composable(schoolClassFormRoute) {
        SchoolClassFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            onAddSchoolYear = navController::navigateToSchoolYearFormRoute
        )
    }
}