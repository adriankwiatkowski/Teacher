package com.example.teacher.feature.student.nav

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacher.core.ui.provider.ActionItemProvider
import com.example.teacher.feature.student.data.StudentScaffoldViewModel
import com.example.teacher.feature.student.nav.StudentNavigation.schoolClassIdArg
import com.example.teacher.feature.student.nav.StudentNavigation.studentIdArg
import com.example.teacher.feature.student.nav.StudentNavigation.studentRoute
import com.example.teacher.feature.student.tab.StudentTab

private const val studentGraphRoute = "student"

private const val studentScreen = "student"
private const val studentFormScreen = "student-form"

object StudentNavigation {
    internal const val schoolClassIdArg = "school-class-id"
    internal const val studentIdArg = "student-id"

    const val studentRoute = "$studentScreen/{$schoolClassIdArg}/{${studentIdArg}}"
}

private const val studentFormRoute =
    "$studentFormScreen/{$schoolClassIdArg}?$studentIdArg={$studentIdArg}"

fun NavController.navigateToStudentGraph(
    schoolClassId: Long,
    studentId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$studentGraphRoute/$schoolClassId/$studentId", navOptions)
}

fun NavController.navigateToStudentFormRoute(
    schoolClassId: Long,
    studentId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (studentId != null) "?$studentIdArg=$studentId" else ""
    this.navigate("$studentFormScreen/$schoolClassId$query", navOptions)
}

fun NavGraphBuilder.studentGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: (message: String) -> Unit,
    navigateToStudentNoteFormRoute: (studentId: Long, studentNoteId: Long?) -> Unit,
) {
    navigation(
        startDestination = studentRoute,
        route = studentGraphRoute,
    ) {
        composable(
            studentRoute,
            arguments = listOf(
                navArgument(schoolClassIdArg) {
                    type = NavType.LongType
                },
                navArgument(studentIdArg) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val viewModel: StudentScaffoldViewModel = hiltViewModel()
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)
            val studentId = args.getLong(studentIdArg)

            val onEditClick = {
                navController.navigateToStudentFormRoute(
                    schoolClassId = schoolClassId,
                    studentId = studentId,
                )
            }

            StudentScaffoldWrapper(
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                menuItems = listOf(
                    ActionItemProvider.edit(onClick = onEditClick),
                    ActionItemProvider.delete(onClick = viewModel::onDeleteStudent),
                ),
                viewModel = viewModel,
            ) { selectedTab, student ->
                when (selectedTab) {
                    StudentTab.Detail -> StudentDetailRoute(
                        snackbarHostState = snackbarHostState,
                        student = student,
                    )

                    StudentTab.Grades -> StudentGradesRoute(
                        snackbarHostState = snackbarHostState,
                        student = student,
                    )

                    StudentTab.Notes -> StudentNotesRoute(
                        snackbarHostState = snackbarHostState,
                        onNoteClick = { studentNoteId ->
                            navigateToStudentNoteFormRoute(studentId, studentNoteId)
                        },
                        onAddNoteClick = {
                            navigateToStudentNoteFormRoute(studentId, null)
                        },
                    )
                }
            }
        }
    }

    composable(
        studentFormRoute,
        arguments = listOf(
            navArgument(schoolClassIdArg) {
                type = NavType.LongType
            },
            navArgument(studentIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) {
        StudentFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
        )
    }
}