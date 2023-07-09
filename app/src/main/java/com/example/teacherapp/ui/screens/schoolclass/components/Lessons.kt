package com.example.teacherapp.ui.screens.schoolclass.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.teacherapp.data.models.entities.BasicLesson
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.utils.expandableItems
import com.example.teacherapp.ui.screens.paramproviders.BasicLessonsPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme

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
        key = { lesson -> "lesson-${lesson.id}" }
    ) { contentPadding, lesson ->
        Card(Modifier.fillMaxWidth()) {
            LessonItem(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                name = lesson.name,
                onLessonClick = { onLessonClick(lesson.id) },
            )
        }
    }

    item {
        Card {
            TeacherOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = onAddLessonClick,
            ) {
                Text(text = "Dodaj zajęcia")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LessonItem(
    name: String,
    onLessonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = onLessonClick,
    ) {
        Box(Modifier.padding(8.dp)) {
            Text(text = name)
        }
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
            val expanded = remember { mutableStateOf(false) }
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