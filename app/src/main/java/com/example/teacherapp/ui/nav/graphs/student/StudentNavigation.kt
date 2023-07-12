package com.example.teacherapp.ui.nav.graphs.student

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.ui.nav.graphs.student.route.StudentFormRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentNoteFormRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentRoute

private const val studentGraphRoute = "student"

internal const val schoolClassIdArg = "school-class-id"
internal const val studentIdArg = "student-id"
internal const val studentNoteIdArg = "student-note-id"

private const val studentDetailScreen = "student"
internal const val studentDetailRoute = "$studentDetailScreen/{$schoolClassIdArg}/{${studentIdArg}}"

private const val studentFormScreen = "student-form"
private const val studentFormRoute =
    "$studentFormScreen/{$schoolClassIdArg}?$studentIdArg={$studentIdArg}"

private const val studentNoteFormScreen = "student-note-form"
private const val studentNoteFormRoute =
    "$studentNoteFormScreen/{$studentIdArg}?$studentNoteIdArg={$studentNoteIdArg}"

fun NavController.navigateToStudentGraph(
    schoolClassId: Long,
    studentId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$studentDetailScreen/$schoolClassId/$studentId", navOptions)
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
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
) {
    navigation(
        startDestination = studentDetailRoute,
        route = studentGraphRoute,
    ) {
        composable(
            studentDetailRoute,
            arguments = listOf(
                navArgument(schoolClassIdArg) {
                    type = NavType.LongType
                },
                navArgument(studentIdArg) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)
            val studentId = args.getLong(studentIdArg)

            StudentRoute(
                onEditClick = {
                    navController.navigateToStudentFormRoute(
                        schoolClassId = schoolClassId,
                        studentId = studentId
                    )
                },
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
                onNavBack = navController::popBackStack,
                setTitle = setTitle,
                showSnackbar = onShowSnackbar,
                addActionMenuItems = addActionMenuItems,
                removeActionMenuItems = removeActionMenuItems,
            )
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
                setTitle = setTitle,
                showSnackbar = onShowSnackbar,
                addActionMenuItems = addActionMenuItems,
                removeActionMenuItems = removeActionMenuItems,
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