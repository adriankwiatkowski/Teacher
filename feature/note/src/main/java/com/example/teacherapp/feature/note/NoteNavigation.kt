package com.example.teacherapp.feature.note

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.feature.note.NoteNavigation.notesRoute

private const val notesScreen = "notes"

object NoteNavigation {
    const val notesRoute = notesScreen
}

fun NavController.navigateToNotesRoute(navOptions: NavOptions? = null) {
    this.navigate(notesRoute, navOptions)
}

fun NavGraphBuilder.noteGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    composable(notesRoute) {
        NotesRoute(
            onNoteClick = { noteId ->

            },
            onAddNoteClick = {

            }
        )
    }
}