package com.example.teacherapp.feature.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.note.data.NotesViewModel

@Composable
internal fun NotesRoute(
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val notesResult by viewModel.notesResult.collectAsStateWithLifecycle()

    NotesScreen(
        notesResult = notesResult,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
    )
}