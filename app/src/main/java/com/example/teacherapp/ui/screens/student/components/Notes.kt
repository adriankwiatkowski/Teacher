package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.ui.Modifier
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.ui.components.expandablelist.expandableItems

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
        // TODO: Consider replacing custom component with built-in ListItem.
//        ListItem(
//            modifier = Modifier
//                .clickable(onClick = { onNoteClick(studentNote.id) })
//                .padding(contentPadding),
//            text = {
//                Text(text = studentNote.title)
//            },
//        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onNoteClick(studentNote.id) })
                .padding(contentPadding)
                .minimumInteractiveComponentSize(),
        ) {
            Text(text = studentNote.title)
        }
    }
}