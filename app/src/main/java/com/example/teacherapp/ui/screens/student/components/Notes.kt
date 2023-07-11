package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.ui.Modifier
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.ui.components.expandablelist.expandableItems

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.notes(
    studentNotes: List<BasicStudentNote>,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    expandableItems(
        modifier = modifier,
        label = "Uwagi",
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        additionalIcon = {
            IconButton(onClick = onAddNoteClick) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                )
            }
        },
        items = studentNotes,
        key = { studentNote -> studentNote.id },
    ) { contentPadding, studentNote ->
        Card(
            modifier = Modifier.fillMaxSize(),
            onClick = { onNoteClick(studentNote.id) },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
            ) {
                Text(text = studentNote.title)
            }
        }
    }
}