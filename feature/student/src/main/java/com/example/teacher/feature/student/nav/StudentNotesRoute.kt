package com.example.teacher.feature.student.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.feature.student.StudentNotesScreen
import com.example.teacher.feature.student.data.StudentNotesViewModel

@Composable
internal fun StudentNotesRoute(
    snackbarHostState: SnackbarHostState,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    viewModel: StudentNotesViewModel = hiltViewModel(),
) {
    val studentNotesResult by viewModel.studentNotesResult.collectAsStateWithLifecycle()

    StudentNotesScreen(
        studentNotesResult = studentNotesResult,
        snackbarHostState = snackbarHostState,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
    )
}