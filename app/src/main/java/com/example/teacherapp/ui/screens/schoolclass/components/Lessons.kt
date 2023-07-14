package com.example.teacherapp.ui.screens.schoolclass.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.ui.components.expandablelist.expandableItems
import com.example.teacherapp.ui.screens.paramproviders.BasicLessonsPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

fun LazyListScope.lessons(
    lessons: List<BasicLesson>,
    onLessonClick: (Long) -> Unit,
    onAddLessonClick: () -> Unit,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    expandableItems(
        modifier = modifier,
        label = "Zajęcia (${lessons.size})",
        expanded = expanded,
        items = lessons,
        key = { lesson -> "lesson-${lesson.id}" },
        additionalIcon = {
            IconButton(onClick = onAddLessonClick) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                )
            }
        },
    ) { contentPadding, lesson ->
        LessonItem(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { onLessonClick(lesson.id) })
                .padding(contentPadding)
                .minimumInteractiveComponentSize(),
            name = lesson.name,
        )
    }
}

@Composable
private fun LessonItem(
    name: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = MaterialTheme.spacing.large),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = name)

        Spacer(modifier = Modifier.weight(1f))

        // TODO: Add students count.
        Text(text = "(0)")
        Icon(imageVector = Icons.Default.Person, contentDescription = null)
    }
}

@Preview
@Composable
private fun LessonItemPreview(
    @PreviewParameter(
        BasicLessonsPreviewParameterProvider::class,
        limit = 4,
    ) lessons: List<BasicLesson>,
) {
    TeacherAppTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }

            LazyColumn {
                lessons(
                    lessons = lessons,
                    onLessonClick = {},
                    onAddLessonClick = {},
                    expanded = expanded,
                )
            }
        }
    }
}