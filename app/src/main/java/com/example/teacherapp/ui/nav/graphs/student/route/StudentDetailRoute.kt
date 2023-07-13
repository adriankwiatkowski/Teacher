package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.student.StudentDetailScreen
import com.example.teacherapp.ui.screens.student.data.StudentDetailViewModel

@Composable
internal fun StudentDetailRoute(
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentDetailViewModel = hiltViewModel(),
) {
    val studentResource by viewModel.studentResource.collectAsStateWithLifecycle()
    val studentNotesResource by viewModel.studentNotesResource.collectAsStateWithLifecycle()

    StudentDetailScreen(
        modifier = modifier,
        studentResource = studentResource,
        studentNotesResource = studentNotesResource,
        onEmailClick = {},
        onPhoneClick = {},
        onGradeClick = {},
        onAddGradeClick = {},
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
        isGradesExpanded = viewModel.isGradesExpanded,
        isNotesExpanded = viewModel.isNotesExpanded,
    )
}