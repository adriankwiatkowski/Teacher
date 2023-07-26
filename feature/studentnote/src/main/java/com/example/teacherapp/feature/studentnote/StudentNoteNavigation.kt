package com.example.teacherapp.feature.studentnote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacherapp.feature.studentnote.StudentNoteNavigation.studentIdArg
import com.example.teacherapp.feature.studentnote.StudentNoteNavigation.studentNoteIdArg
import com.example.teacherapp.feature.studentnote.route.StudentNoteFormRoute

private const val studentNoteFormScreen = "student-note-form"

internal object StudentNoteNavigation {
    internal const val studentIdArg = "student-id"
    internal const val studentNoteIdArg = "student-note-id"
}

private const val studentNoteFormRoute =
    "$studentNoteFormScreen/{$studentIdArg}?$studentNoteIdArg={$studentNoteIdArg}"

fun NavController.navigateToStudentNoteFormRoute(
    studentId: Long,
    studentNoteId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (studentNoteId != null) "?$studentNoteIdArg=$studentNoteId" else ""
    this.navigate("$studentNoteFormScreen/$studentId$query", navOptions)
}

fun NavGraphBuilder.studentNoteGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
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