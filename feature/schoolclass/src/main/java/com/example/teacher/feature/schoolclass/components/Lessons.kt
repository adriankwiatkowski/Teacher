package com.example.teacher.feature.schoolclass.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.ui.component.expandablelist.expandableItems
import com.example.teacher.core.ui.paramprovider.BasicLessonsPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

internal fun LazyListScope.lessons(
    lessons: List<BasicLesson>,
    studentCount: Long,
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
            studentCount = studentCount,
        )
    }
}

@Composable
private fun LessonItem(
    name: String,
    studentCount: Long,
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

        Text(text = "($studentCount)")
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
    TeacherTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }

            LazyColumn {
                lessons(
                    lessons = lessons,
                    studentCount = 4,
                    onLessonClick = {},
                    onAddLessonClick = {},
                    expanded = expanded,
                )
            }
        }
    }
}