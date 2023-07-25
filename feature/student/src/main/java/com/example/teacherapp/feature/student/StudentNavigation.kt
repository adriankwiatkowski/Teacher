package com.example.teacherapp.feature.student

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacherapp.core.studentcommon.StudentConstants.schoolClassIdArg
import com.example.teacherapp.core.studentcommon.StudentConstants.studentIdArg
import com.example.teacherapp.core.studentcommon.StudentConstants.studentNoteIdArg
import com.example.teacherapp.core.ui.provider.ActionItemProvider
import com.example.teacherapp.feature.student.StudentNavigation.studentRoute
import com.example.teacherapp.feature.student.data.StudentScaffoldViewModel
import com.example.teacherapp.feature.student.route.StudentDetailRoute
import com.example.teacherapp.feature.student.route.StudentFormRoute
import com.example.teacherapp.feature.student.route.StudentGradesRoute
import com.example.teacherapp.feature.student.route.StudentScaffoldWrapper
import com.example.teacherapp.feature.student.tab.StudentTab
import com.example.teacherapp.feature.studentnote.route.StudentNoteFormRoute
import com.example.teacherapp.feature.studentnote.route.StudentNotesRoute

private const val studentGraphRoute = "student"

private const val studentScreen = "student"
private const val studentFormScreen = "student-form"
private const val studentNoteFormScreen = "student-note-form"

object StudentNavigation {
    const val studentRoute = "$studentScreen/{$schoolClassIdArg}/{${studentIdArg}}"
}

private const val studentFormRoute =
    "$studentFormScreen/{$schoolClassIdArg}?$studentIdArg={$studentIdArg}"

private const val studentNoteFormRoute =
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
                    ActionItemProvider.edit(onClick = onEditClick),
                    ActionItemProvider.delete(onClick = viewModel::onDeleteStudent),
                ),
                viewModel = viewModel,
            ) { selectedTab, student ->
                when (selectedTab) {
                    StudentTab.Detail -> StudentDetailRoute(student = student)

                    StudentTab.Grades -> StudentGradesRoute(student = student)

                    StudentTab.Notes -> StudentNotesRoute(
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
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val isEditMode = args.getLong(studentNoteIdArg) != 0L

            StudentNoteFormRoute(
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                isEditMode = isEditMode,
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
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
        )
    }
}