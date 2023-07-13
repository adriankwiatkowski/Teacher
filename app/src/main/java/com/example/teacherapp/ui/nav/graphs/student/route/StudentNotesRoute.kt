package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.student.note.StudentNotesScreen
import com.example.teacherapp.ui.screens.student.note.data.StudentNotesViewModel

@Composable
internal fun StudentNotesRoute(
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentNotesViewModel = hiltViewModel(),
) {
    val studentNotesResource by viewModel.studentNotesResource.collectAsStateWithLifecycle()

    StudentNotesScreen(
        modifier = modifier,
        studentNotesResource = studentNotesResource,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
    )
}