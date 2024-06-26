package com.example.teacher.feature.studentnote.nav

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.studentnote.nav.StudentNoteNavigation.studentIdArg
import com.example.teacher.feature.studentnote.nav.StudentNoteNavigation.studentNoteIdArg

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
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
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
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            isEditMode = isEditMode,
        )
    }
}