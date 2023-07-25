package com.example.teacherapp.feature.lesson

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.lesson.paramprovider.LessonsPreviewParameterProvider

// TODO: Currently this screen is unused, consider deleting or using in school class tab.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonsScreen(
    lessons: List<Lesson>,
    onLessonClick: (id: Long) -> Unit,
    onAddLessonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        items(lessons, key = { it.id }) { lesson ->
            LessonItem(
                modifier = Modifier
                    .fillMaxWidth(),
                name = lesson.name,
                onClick = { onLessonClick(lesson.id) },
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAddLessonClick
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Dodaj przedmiot")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonItem(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(Modifier.padding(MaterialTheme.spacing.small)) {
            Text(name)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonsScreenPreview(
    @PreviewParameter(LessonsPreviewParameterProvider::class) lessons: List<Lesson>,
) {
    TeacherAppTheme {
        Surface {
            LessonsScreen(
                modifier = Modifier.fillMaxSize(),
                lessons = lessons,
                onLessonClick = {},
                onAddLessonClick = {},
            )
        }
    }
}