package com.example.teacher.feature.schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonsByYear
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.LessonsByYearPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonPickerScreen(
    lessonsByYearResult: Result<List<LessonsByYear>>,
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
                title = stringResource(R.string.schedule_event_form_title),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = modifier,
            result = lessonsByYearResult,
        ) { lessonsByYear ->
            MainScreen(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small),
                lessonsByYear = lessonsByYear,
                onLessonClick = onLessonClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreen(
    lessonsByYear: List<LessonsByYear>,
    onLessonClick: (lessonId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (lessonsByYear.isEmpty()) {
        EmptyState(modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        item {
            Text(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                text = stringResource(R.string.schedule_pick_lesson),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        for ((yearIndex, lessonsInYear) in lessonsByYear.withIndex()) {
            val lessonsBySchoolClass = lessonsInYear.lessonsBySchoolClass

            stickyHeader(key = "sy-${lessonsInYear.year.id}") {
                Text(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small),
                    text = lessonsInYear.year.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }

            for ((schoolClassIndex, lessonInSchoolClass) in lessonsBySchoolClass.withIndex()) {
                val isFirstIndex = schoolClassIndex == 0
                val isLastIndex = schoolClassIndex == lessonsBySchoolClass.lastIndex
                if (!isFirstIndex && !isLastIndex) {
                    item(key = "sc-${lessonInSchoolClass.schoolClass.id}") {
                        Divider()
                    }
                }

                items(
                    items = lessonInSchoolClass.lessons,
                    key = { lessons -> lessons.id },
                ) { lesson ->
                    LessonItem(lesson = lesson, onClick = { onLessonClick(lesson.id) })
                }
            }

            if (yearIndex != lessonsByYear.lastIndex) {
                item {
                    Divider()
                    Spacer(Modifier.padding(MaterialTheme.spacing.small))
                }
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
        headlineContent = { Text("${lesson.name} ${lesson.schoolClass.name}") },
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TeacherLargeText(text = stringResource(R.string.schedule_no_lesson_exists))
    }
}

@Preview
@Composable
private fun LessonPickerScreenPreview(
    @PreviewParameter(
        LessonsByYearPreviewParameterProvider::class
    ) lessonsByYear: List<LessonsByYear>,
) {
    TeacherTheme {
        Surface {
            LessonPickerScreen(
                lessonsByYearResult = Result.Success(lessonsByYear),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onLessonClick = {},
            )
        }
    }
}