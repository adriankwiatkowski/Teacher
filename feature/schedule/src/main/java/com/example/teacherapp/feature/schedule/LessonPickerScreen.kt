package com.example.teacherapp.feature.schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.LessonsPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonPickerScreen(
    lessonsResult: Result<List<Lesson>>,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onLessonClick: (lessonId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = "Dodaj termin zajęć",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = modifier,
            result = lessonsResult,
        ) { lessons ->
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreen(
    lessons: List<Lesson>,
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
        stickyHeader {
            Text(
                text = "Wybierz przedmiot",
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        itemsIndexed(items = lessons, key = { _, lessons -> lessons.id }) { index, lesson ->
            LessonItem(lesson = lesson, onClick = { onLessonClick(lesson.id) })
            if (index != lessons.lastIndex) {
                Divider()
            }
        }
    }
}

@Composable
private fun LessonItem(
    lesson: Lesson,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = { Text(lesson.name) },
        supportingContent = { Text(lesson.schoolClass.name) },
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Nie istnieje jeszcze żaden przedmiot. Musi istnieć przedmiot, żeby można było stworzyć plan zajęć.",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Preview
@Composable
private fun LessonPickerScreenPreview(
    @PreviewParameter(LessonsPreviewParameterProvider::class) lessons: List<Lesson>,
) {
    TeacherAppTheme {
        Surface {
            LessonPickerScreen(
                lessonsResult = Result.Success(lessons),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onLessonClick = {},
            )
        }
    }
}