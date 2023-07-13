package com.example.teacherapp.ui.screens.student.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.ui.components.TeacherFab
import com.example.teacherapp.ui.components.resource.ResourceContent
import com.example.teacherapp.ui.screens.paramproviders.BasicStudentNotesPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun StudentNotesScreen(
    studentNotesResource: Resource<List<BasicStudentNote>>,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResourceContent(
        modifier = modifier,
        resource = studentNotesResource,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainScreen(
    notes: List<BasicStudentNote>,
    onNoteClick: (noteId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        itemsIndexed(items = notes, key = { _, note -> note.id }) { index, note ->
            ListItem(
                modifier = Modifier.clickable { onNoteClick(note.id) },
                text = { Text(note.title) }
            )
            if (index != notes.lastIndex) {
                Divider()
            }
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
                studentNotesResource = Resource.Success(studentNotes),
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }
    }
}