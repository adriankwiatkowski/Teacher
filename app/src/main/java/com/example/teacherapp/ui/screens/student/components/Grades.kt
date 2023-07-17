package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teacherapp.ui.components.expandablelist.ExpandableItem
import com.example.teacherapp.ui.components.expandablelist.expandableItem
import com.example.teacherapp.ui.theme.spacing

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.grades(
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    onGradeClick: () -> Unit,
    onAddGradeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    expandableItem(
        modifier = modifier,
        label = "Oceny",
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        additionalIcon = {
            IconButton(onClick = onAddGradeClick) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
        ) {
            ExpandableItem(
                label = "Matematyka (4,50)",
                expanded = true,
                toggleExpanded = {},
            ) { contentPadding ->
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    GradeBox(grade = "3", onClick = onGradeClick)
                    GradeBox(grade = "4", onClick = onGradeClick)
                    GradeBox(grade = "5", onClick = onGradeClick)
                    GradeBox(grade = "6", onClick = onGradeClick)
                    GradeBox(grade = "3+", onClick = onGradeClick)
                    GradeBox(grade = "4+", onClick = onGradeClick)
                    GradeBox(grade = "6-", onClick = onGradeClick)
                }
            }
            ExpandableItem(
                label = "Geografia (4,10)",
                expanded = false,
                toggleExpanded = {},
            ) {
            }
        }
    }
}

@Composable
private fun GradeBox(
    grade: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Text(grade)
    }
}