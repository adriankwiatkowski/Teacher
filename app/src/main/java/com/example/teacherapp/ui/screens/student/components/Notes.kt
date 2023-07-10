package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.ui.Modifier
import com.example.teacherapp.ui.components.expandablelist.expandableItem

fun LazyListScope.notes(
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    onNoteClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    expandableItem(
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
        }
    ) { contentPadding ->
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(contentPadding)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNoteClick),
                    text = "Notatka #1",
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNoteClick),
                    text = "Notatka #2",
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNoteClick),
                    text = "Notatka #3",
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNoteClick),
                    text = "Notatka #4",
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNoteClick),
                    text = "Notatka #5",
                )
            }
        }
    }
}