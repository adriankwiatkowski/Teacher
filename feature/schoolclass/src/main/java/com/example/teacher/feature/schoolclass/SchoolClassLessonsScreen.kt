package com.example.teacher.feature.schoolclass

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.paramprovider.BasicLessonsPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schoolclass.components.LessonItem

@Composable
internal fun SchoolClassLessonsScreen(
    snackbarHostState: SnackbarHostState,
    lessons: List<BasicLesson>,
    onLessonClick: (id: Long) -> Unit,
    onAddLessonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = { TeacherFab(TeacherActions.add(onClick = onAddLessonClick)) },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        MainScreen(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small),
            lessons = lessons,
            onLessonClick = onLessonClick,
        )
    }
}

@Composable
private fun MainScreen(
    lessons: List<BasicLesson>,
    onLessonClick: (lessonId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (lessons.isEmpty()) {
        EmptyState(modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        items(items = lessons, key = { lessons -> lessons.id }) { lesson ->
            LessonItem(
                name = lesson.name,
                onClick = { onLessonClick(lesson.id) },
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TeacherLargeText(text = stringResource(R.string.school_class_no_lessons_in_class))
    }
}

@Preview
@Composable
private fun SchoolClassLessonsScreenPreview(
    @PreviewParameter(
        BasicLessonsPreviewParameterProvider::class,
        limit = 1,
    ) lessons: List<BasicLesson>,
) {
    TeacherTheme {
        Surface {
            SchoolClassLessonsScreen(
                snackbarHostState = remember { SnackbarHostState() },
                lessons = lessons,
                onLessonClick = {},
                onAddLessonClick = {},
            )
        }
    }
}