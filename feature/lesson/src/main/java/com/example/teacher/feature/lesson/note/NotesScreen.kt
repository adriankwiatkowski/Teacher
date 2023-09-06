package com.example.teacher.feature.lesson.note

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicLessonNote
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.LessonNotesPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.R

@Composable
internal fun NotesScreen(
    notesResult: Result<List<BasicLessonNote>>,
    snackbarHostState: SnackbarHostState,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier.padding(MaterialTheme.spacing.small),
        result = notesResult,
    ) { notes ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                TeacherFab(action = TeacherActions.add(onClick = onAddNoteClick))
            },
            floatingActionButtonPosition = FabPosition.End,
        ) { innerPadding ->
            if (notes.isEmpty()) {
                EmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            } else {
                MainContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    notes = notes,
                    onNoteClick = onNoteClick,
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    notes: List<BasicLessonNote>,
    onNoteClick: (noteId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        items(notes, key = { note -> note.id }) { note ->
            NoteItem(
                note = note,
                onClick = { onNoteClick(note.id) },
            )
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(R.string.lesson_empty_notes))
    }
}

@Composable
private fun NoteItem(
    note: BasicLessonNote,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = { Text(note.title) },
        supportingContent = if (note.text.isNotBlank()) {
            {
                Text(
                    text = note.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else {
            null
        },
    )
}

@Preview
@Composable
private fun NotesScreenPreview(
    @PreviewParameter(
        LessonNotesPreviewParameterProvider::class
    ) notes: List<BasicLessonNote>
) {
    TeacherTheme {
        Surface {
            NotesScreen(
                notesResult = Result.Success(notes),
                snackbarHostState = remember { SnackbarHostState() },
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }
    }
}