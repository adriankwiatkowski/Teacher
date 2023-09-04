package com.example.teacher.core.ui.component.expandablelist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.theme.TeacherTheme

fun LazyListScope.expandableItem(
    label: String,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    additionalIcon: @Composable (() -> Unit)? = null,
    content: @Composable LazyItemScope.(contentPadding: PaddingValues) -> Unit,
) {
    expandableItem(
        modifier = modifier,
        label = label,
        expanded = expanded.value,
        toggleExpanded = { expanded.value = !expanded.value },
        additionalIcon = additionalIcon,
        content = content
    )
}

fun LazyListScope.expandableItem(
    label: String,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    additionalIcon: @Composable (() -> Unit)? = null,
    content: @Composable LazyItemScope.(contentPadding: PaddingValues) -> Unit,
) {
    expandableContent(
        modifier = modifier,
        label = label,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        additionalIcon = additionalIcon,
    ) { contentPadding ->
        item {
            content(contentPadding)
        }
    }
}

fun <T> LazyListScope.expandableItems(
    label: String,
    expanded: MutableState<Boolean>,
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    additionalIcon: @Composable (() -> Unit)? = null,
    itemContent: @Composable LazyItemScope.(
        contentPadding: PaddingValues,
        item: T,
    ) -> Unit,
) {
    expandableItems(
        modifier = modifier,
        label = label,
        expanded = expanded.value,
        toggleExpanded = { expanded.value = !expanded.value },
        items = items,
        key = key,
        contentType = contentType,
        additionalIcon = additionalIcon,
        itemContent = itemContent
    )
}

fun <T> LazyListScope.expandableItems(
    label: String,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    additionalIcon: @Composable (() -> Unit)? = null,
    itemContent: @Composable LazyItemScope.(
        contentPadding: PaddingValues,
        item: T,
    ) -> Unit,
) {
    expandableContent(
        modifier = modifier,
        label = label,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        additionalIcon = additionalIcon,
    ) { contentPadding ->
        items(items, key = key, contentType = contentType) { item ->
            itemContent(contentPadding, item)
        }
    }
}

private inline fun LazyListScope.expandableContent(
    label: String,
    expanded: Boolean,
    noinline toggleExpanded: () -> Unit,
    noinline additionalIcon: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: LazyListScope.(contentPadding: PaddingValues) -> Unit,
) {
    val icon =
        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val contentPadding = PaddingValues(start = icon.defaultWidth)

    item {
        ExpandableLabel(
            modifier = modifier,
            label = label,
            toggleExpanded = toggleExpanded,
            additionalIcon = additionalIcon,
            icon = icon,
            contentDescription = null,
        )
    }

    if (expanded) {
        content(contentPadding)
    }
}

@Preview
@Composable
private fun LazyExpandableItemPreview() {
    TeacherTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }

            LazyColumn {
                expandableItem(
                    label = "Label",
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
                        text = "Hello World",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LazyExpandableItemsPreview() {
    TeacherTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }
            val items = List(10) { i ->
                "Hello World #${i + 1}"
            }

            LazyColumn {
                expandableItems(
                    label = "Label",
                    expanded = expanded,
                    items = items,
                    key = { it },
                ) { contentPadding, item ->
                    Text(
                        modifier = Modifier.padding(contentPadding),
                        text = item,
                    )
                }
            }
        }
    }
}