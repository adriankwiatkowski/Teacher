package com.example.teacherapp.feature.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.student.note.StudentNotesScreen
import com.example.teacherapp.feature.student.note.data.StudentNotesViewModel

@Composable
internal fun StudentNotesRoute(
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    viewModel: StudentNotesViewModel = hiltViewModel(),
) {
    val studentNotesResult by viewModel.studentNotesResult.collectAsStateWithLifecycle()

    StudentNotesScreen(
        studentNotesResult = studentNotesResult,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
    )
}