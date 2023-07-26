package com.example.teacherapp.feature.note.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.feature.note.nav.NoteNavigation.noteIdArg
import com.example.teacherapp.feature.note.nav.NoteNavigation.notesRoute

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
    this.navigate("$noteFormRoute$query", navOptions)
}

fun NavGraphBuilder.noteGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    composable(notesRoute) {
        NotesRoute(
            onNoteClick = { noteId -> navController.navigateToNoteFormRoute(noteId = noteId) },
            onAddNoteClick = { navController.navigateToNoteFormRoute(noteId = null) },
        )
    }

    composable(noteFormRoute) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val isEditMode = args.getLong(noteIdArg) != 0L

        NoteFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            isEditMode = isEditMode,
        )
    }
}