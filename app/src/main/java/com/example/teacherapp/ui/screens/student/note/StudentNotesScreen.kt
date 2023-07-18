package com.example.teacherapp.ui.screens.student.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicStudentNote
import com.example.teacherapp.ui.components.TeacherFab
import com.example.teacherapp.ui.components.result.ResultContent
import com.example.teacherapp.ui.screens.paramproviders.BasicStudentNotesPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun StudentNotesScreen(
    studentNotesResult: Result<List<BasicStudentNote>>,
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
        itemsIndexed(items = notes, key = { _, note -> note.id }) { index, note ->
            ListItem(
                modifier = Modifier.clickable { onNoteClick(note.id) },
                headlineContent = { Text(note.title) },
            )
            if (index != notes.lastIndex) {
                Divider()
            }
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
            text = "Uczeń nie ma żadnej uwagi",
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    TeacherAppTheme {
        Surface {
            EmptyState()
        }
    }
}

@Preview
@Composable
private fun StudentNotesScreenPreview(
    @PreviewParameter(BasicStudentNotesPreviewParameterProvider::class) studentNotes: List<BasicStudentNote>
) {
    TeacherAppTheme {
        Surface {
            StudentNotesScreen(
                studentNotesResult = Result.Success(studentNotes),
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }
    }
}