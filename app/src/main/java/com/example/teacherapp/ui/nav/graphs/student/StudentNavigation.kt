package com.example.teacherapp.ui.nav.graphs.student

import androidx.compose.material.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation.schoolClassIdArg
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation.studentFormRoute
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation.studentIdArg
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation.studentNoteIdArg
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation.studentRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentDetailRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentFormRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentNoteFormRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentNotesRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentScaffoldWrapper
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab
import com.example.teacherapp.ui.screens.student.data.StudentScaffoldViewModel

private const val studentGraphRoute = "student"

private const val studentScreen = "student"
private const val studentFormScreen = "student-form"
private const val studentNoteFormScreen = "student-note-form"

internal object StudentNavigation {
    internal const val schoolClassIdArg = "school-class-id"
    internal const val studentIdArg = "student-id"
    internal const val studentNoteIdArg = "student-note-id"

    internal const val studentRoute = "$studentScreen/{$schoolClassIdArg}/{${studentIdArg}}"
    internal const val studentFormRoute =
        "$studentFormScreen/{$schoolClassIdArg}?$studentIdArg={$studentIdArg}"
}

const val studentNoteFormRoute =
    "$studentNoteFormScreen/{$studentIdArg}?$studentNoteIdArg={$studentNoteIdArg}"

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

private fun NavController.navigateToStudentNoteFormRoute(
    studentId: Long,
    studentNoteId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (studentNoteId != null) "?$studentNoteIdArg=$studentNoteId" else ""
    this.navigate("$studentNoteFormScreen/$studentId$query", navOptions)
}

fun NavGraphBuilder.studentGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
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
                    ActionMenuItemProvider.edit(onClick = onEditClick),
                    ActionMenuItemProvider.delete(onClick = viewModel::onDeleteStudent),
                ),
                viewModel = viewModel,
            ) { selectedTab, student ->
                when (selectedTab) {
                    StudentTab.Detail -> {
                        StudentDetailRoute(student = student)
                    }

                    StudentTab.Grades -> {
                        Text("Grades")
                    }

                    StudentTab.Notes -> {
                        StudentNotesRoute(
                            onNoteClick = { studentNoteId ->
                                navController.navigateToStudentNoteFormRoute(
                                    studentId = studentId,
                                    studentNoteId = studentNoteId,
                                )
                            },
                            onAddNoteClick = {
                                navController.navigateToStudentNoteFormRoute(
                                    studentId = studentId,
                                    studentNoteId = null,
                                )
                            },
                        )
                    }
                }
            }
        }

        composable(
            studentNoteFormRoute,
            arguments = listOf(
                navArgument(studentIdArg) {
                    type = NavType.LongType
                },
                navArgument(studentNoteIdArg) {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            ),
        ) {
            StudentNoteFormRoute(
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
            )
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
            onNavBack = navController::popBackStack,
            setTitle = setTitle,
        )
    }
}