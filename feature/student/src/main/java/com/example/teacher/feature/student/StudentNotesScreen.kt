package com.example.teacher.feature.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicStudentNote
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.BasicStudentNotesPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@Composable
internal fun StudentNotesScreen(
    studentNotesResult: Result<List<BasicStudentNote>>,
    snackbarHostState: SnackbarHostState,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier,
        result = studentNotesResult,
    ) { studentNotes ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = { TeacherFab(TeacherActions.add(onClick = onAddNoteClick)) },
            floatingActionButtonPosition = FabPosition.End,
        ) { innerPadding ->
            MainScreen(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small),
                notes = studentNotes,
                onNoteClick = onNoteClick,
            )
        }
    }
}

@Composable
private fun MainScreen(
    notes: List<BasicStudentNote>,
    onNoteClick: (noteId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (notes.isEmpty()) {
        EmptyState(modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        items(items = notes, key = { note -> note.id }) { note ->
            ListItem(
                modifier = Modifier.clickable { onNoteClick(note.id) },
                headlineContent = { Text(note.title) },
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.student_empty_notes),
            style = MaterialTheme.typography.displayMedium,
        )
    }
}

@Preview
@Composable
private fun StudentNotesScreenPreview(
    @PreviewParameter(BasicStudentNotesPreviewParameterProvider::class) studentNotes: List<BasicStudentNote>
) {
    TeacherTheme {
        Surface {
            StudentNotesScreen(
                studentNotesResult = Result.Success(studentNotes),
                snackbarHostState = remember { SnackbarHostState() },
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    TeacherTheme {
        Surface {
            StudentNotesScreen(
                studentNotesResult = Result.Success(emptyList()),
                snackbarHostState = remember { SnackbarHostState() },
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }
    }
}