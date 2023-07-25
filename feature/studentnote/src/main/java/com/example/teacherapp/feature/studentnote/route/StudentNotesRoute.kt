package com.example.teacherapp.feature.studentnote.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.studentnote.StudentNotesScreen
import com.example.teacherapp.feature.studentnote.data.StudentNotesViewModel

@Composable
fun StudentNotesRoute(
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
) {
    val viewModel = hiltViewModel<StudentNotesViewModel>()
    val studentNotesResult by viewModel.studentNotesResult.collectAsStateWithLifecycle()

    StudentNotesScreen(
        studentNotesResult = studentNotesResult,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
    )
}