package com.example.teacher.feature.note.nav

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.note.nav.NoteNavigation.noteIdArg
import com.example.teacher.feature.note.nav.NoteNavigation.notesRoute

private const val notesScreen = "notes"
private const val noteFormScreen = "note-form"

object NoteNavigation {
    internal const val noteIdArg = "note-id"

    const val notesRoute = notesScreen
}

private const val noteFormRoute = "$noteFormScreen?$noteIdArg={$noteIdArg}"

fun NavController.navigateToNotesRoute(navOptions: NavOptions? = null) {
    this.navigate(notesRoute, navOptions)
}

private fun NavController.navigateToNoteFormRoute(
    noteId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (noteId != null) "?$noteIdArg=$noteId" else ""
    this.navigate("$noteFormScreen$query", navOptions)
}

fun NavGraphBuilder.noteGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
) {
    composable(notesRoute) {
        NotesRoute(
            snackbarHostState = snackbarHostState,
            onNoteClick = { noteId -> navController.navigateToNoteFormRoute(noteId = noteId) },
            onAddNoteClick = { navController.navigateToNoteFormRoute(noteId = null) },
        )
    }

    composable(
        noteFormRoute,
        arguments = listOf(
            navArgument(noteIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val isEditMode = args.getLong(noteIdArg) != 0L

        NoteFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            isEditMode = isEditMode,
        )
    }
}