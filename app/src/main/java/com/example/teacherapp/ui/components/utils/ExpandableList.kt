package com.example.teacherapp.ui.components.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teacherapp.ui.theme.TeacherAppTheme

fun LazyListScope.expandableItem(
    label: String,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    content: @Composable LazyItemScope.(contentPadding: PaddingValues) -> Unit,
) {
    expandableItem(
        label = label,
        expanded = expanded.value,
        toggleExpanded = { expanded.value = !expanded.value },
        modifier = modifier,
        content = content
    )
}

fun LazyListScope.expandableItem(
    label: String,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable LazyItemScope.(contentPadding: PaddingValues) -> Unit,
) {
    expandableContent(
        modifier = modifier,
        label = label,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
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
    ) { contentPadding ->
        items(items, key = key, contentType = contentType) { item ->
            itemContent(
                contentPadding = contentPadding,
                item = item,
            )
        }
    }
}

private inline fun LazyListScope.expandableContent(
    label: String,
    expanded: Boolean,
    noinline toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    content: LazyListScope.(contentPadding: PaddingValues) -> Unit,
) {
    val icon =
        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val contentPadding = PaddingValues(start = icon.defaultWidth)

    item {
        Card(modifier = modifier) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = toggleExpanded)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                ) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Icon(
                            icon,
                            contentDescription = null,
                        )
                    }

                    Text(text = label)
                }

            }
        }
    }

    if (expanded) {
        content(contentPadding)
    }
}

@Preview
@Composable
private fun ExpandableLazyItemPreview() {
    TeacherAppTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }

            LazyColumn {
                expandableItem(
                    label = "Label",
                    expanded = expanded,
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
private fun ExpandableLazyItemsPreview() {
    TeacherAppTheme {
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