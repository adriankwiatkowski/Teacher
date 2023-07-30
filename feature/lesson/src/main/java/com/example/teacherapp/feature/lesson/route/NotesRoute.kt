package com.example.teacherapp.feature.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.lesson.note.NotesScreen
import com.example.teacherapp.feature.lesson.note.data.NotesViewModel

@Composable
internal fun NotesRoute(
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val lessonNotesResult by viewModel.lessonNotesResult.collectAsStateWithLifecycle()

    NotesScreen(
        notesResult = lessonNotesResult,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick
    )
}