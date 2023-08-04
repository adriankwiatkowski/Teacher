package com.example.teacherapp.feature.lesson.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicLessonNote
import com.example.teacherapp.core.ui.component.TeacherFab
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.LessonNotesPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@Composable
internal fun NotesScreen(
    notesResult: Result<List<BasicLessonNote>>,
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
            floatingActionButton = {
                TeacherFab(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    onClick = onAddNoteClick,
                )
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
        itemsIndexed(
            notes,
            key = { _, note -> note.id },
        ) { index, note ->
            NoteItem(
                note = note,
                onClick = { onNoteClick(note.id) },
            )

            if (index != notes.lastIndex) {
                Divider()
            }
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
        Text(
            text = "Nie napisano jeszcze żadnej notatki",
            style = MaterialTheme.typography.displayMedium,
        )
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
    TeacherAppTheme {
        Surface {
            NotesScreen(
                notesResult = Result.Success(notes),
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }
    }
}