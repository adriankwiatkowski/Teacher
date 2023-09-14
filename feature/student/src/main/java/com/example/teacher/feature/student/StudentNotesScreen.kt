package com.example.teacher.feature.student

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicStudentNote
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.student.data.StudentNotes
import com.example.teacher.feature.student.paramprovider.StudentNotesPreviewParameterProvider

@Composable
internal fun StudentNotesScreen(
    studentNotesResult: Result<StudentNotes>,
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
            if (studentNotes.neutralNotes.isEmpty() && studentNotes.negativeNotes.isEmpty()) {
                EmptyState(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.small),
                )
            } else {
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
}

@Composable
private fun MainScreen(
    notes: StudentNotes,
    onNoteClick: (noteId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        if (notes.neutralNotes.isNotEmpty()) {
            notesHeader(title = context.getString(R.string.student_notes_neutral))
            notes(notes = notes.neutralNotes, onNoteClick = onNoteClick)
        }

        if (notes.negativeNotes.isNotEmpty()) {
            notesHeader(title = context.getString(R.string.student_notes_negative))
            notes(notes = notes.negativeNotes, onNoteClick = onNoteClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.notesHeader(title: String) {
    stickyHeader {
        Surface(modifier = Modifier.fillParentMaxWidth()) {
            Text(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                style = MaterialTheme.typography.headlineSmall,
                text = title,
            )
        }
    }
}

private fun LazyListScope.notes(
    notes: List<BasicStudentNote>,
    onNoteClick: (noteId: Long) -> Unit,
) {
    items(items = notes, key = { note -> note.id }) { note ->
        ListItem(
            modifier = Modifier.clickable { onNoteClick(note.id) },
            headlineContent = { Text(note.title) },
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TeacherLargeText(text = stringResource(R.string.student_empty_notes))
    }
}

@Preview
@Composable
private fun StudentNotesScreenPreview(
    @PreviewParameter(StudentNotesPreviewParameterProvider::class) studentNotes: StudentNotes,
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