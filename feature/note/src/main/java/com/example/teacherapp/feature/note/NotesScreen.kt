package com.example.teacherapp.feature.note

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
import com.example.teacherapp.core.model.data.Note
import com.example.teacherapp.core.model.data.NotePriority
import com.example.teacherapp.core.ui.component.TeacherFab
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.NotesPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@Composable
internal fun NotesScreen(
    notesResult: Result<List<Note>>,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier,
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
            MainScreen(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small),
                notes = notes,
                onNoteClick = onNoteClick,
            )
        }
    }
}

@Composable
private fun MainScreen(
    notes: List<Note>,
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
        itemsIndexed(items = notes, key = { _, note -> note.id }) { index, note ->

            ListItem(
                modifier = Modifier.clickable { onNoteClick(note.id) },
                overlineContent = { Text(priorityToName(note.priority)) },
                headlineContent = { Text(note.title) },
                supportingContent = {
                    Text(
                        text = note.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
            if (index != notes.lastIndex) {
                Divider()
            }
        }
    }
}

private fun priorityToName(notePriority: NotePriority): String {
    return when (notePriority) {
        NotePriority.Low -> "Nieważne"
        NotePriority.Medium -> "Ważne"
        NotePriority.High -> "Bardzo ważne"
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
            text = "Nie napisano jeszcze żadnej notatki",
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Preview
@Composable
private fun NotesScreenPreview(
    @PreviewParameter(NotesPreviewParameterProvider::class) notes: List<Note>
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