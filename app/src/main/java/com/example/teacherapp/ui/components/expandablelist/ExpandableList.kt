package com.example.teacherapp.ui.components.expandablelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun ExpandableItem(
    label: String,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    additionalIcon: @Composable (() -> Unit)? = null,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    ExpandableItem(
        modifier = modifier,
        label = label,
        expanded = expanded.value,
        toggleExpanded = { expanded.value = !expanded.value },
        additionalIcon = additionalIcon,
        content = content
    )
}

@Composable
fun ExpandableItem(
    label: String,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    additionalIcon: @Composable (() -> Unit)? = null,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    ExpandableContent(
        modifier = modifier,
        label = label,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        additionalIcon = additionalIcon,
    ) { contentPadding ->
        content(contentPadding)
    }
}

@Composable
private fun ExpandableContent(
    label: String,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    additionalIcon: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    val icon =
        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val contentPadding = PaddingValues(start = icon.defaultWidth)

    Column(modifier = modifier) {
        ExpandableLabel(
            modifier = modifier,
            label = label,
            toggleExpanded = toggleExpanded,
            additionalIcon = additionalIcon,
            icon = icon,
            contentDescription = null,
        )

        if (expanded) {
            content(contentPadding)
        }
    }
}

@Preview
@Composable
private fun ExpandableItemPreview() {
    TeacherAppTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }

            ExpandableItem(
                label = "Hello World",
                expanded = expanded,
                additionalIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                        )
                    }
                }
            ) { contentPadding ->
                Text(
                    modifier = Modifier.padding(contentPadding),
                    text = "Expanded content",
                )
            }
        }
    }
}