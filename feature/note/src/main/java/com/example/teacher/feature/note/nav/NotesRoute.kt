package com.example.teacher.feature.note.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.feature.note.NotesScreen
import com.example.teacher.feature.note.data.NotesViewModel

@Composable
internal fun NotesRoute(
    snackbarHostState: SnackbarHostState,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val notesResult by viewModel.notesResult.collectAsStateWithLifecycle()

    NotesScreen(
        notesResult = notesResult,
        snackbarHostState = snackbarHostState,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
    )
}