package com.example.teacher.feature.lesson.lessonactivity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonActivity
import com.example.teacher.core.ui.component.TeacherIconButton
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.lessonactivity.data.LessonActivityUiState
import com.example.teacher.feature.lesson.paramprovider.LessonActivityUiStatePreviewParameterProvider

@Composable
internal fun LessonActivityScreen(
    lessonActivityUiStateResult: Result<LessonActivityUiState>,
    lesson: Lesson,
    snackbarHostState: SnackbarHostState,
    onIncreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
    onDecreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier.padding(innerPadding),
            result = lessonActivityUiStateResult,
        ) { lessonActivityUiState ->
            val isEmpty = lessonActivityUiState.firstTermLessonActivities.isEmpty() &&
                    lessonActivityUiState.secondTermLessonActivities.isEmpty()

            if (isEmpty) {
                EmptyState(modifier = Modifier.fillMaxSize())
            } else {
                MainContent(
                    modifier = Modifier.fillMaxSize(),
                    lesson = lesson,
                    firstTermLessonActivities = lessonActivityUiState.firstTermLessonActivities,
                    secondTermLessonActivities = lessonActivityUiState.secondTermLessonActivities,
                    onIncreaseLessonActivity = onIncreaseLessonActivity,
                    onDecreaseLessonActivity = onDecreaseLessonActivity,
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    lesson: Lesson,
    firstTermLessonActivities: List<LessonActivity>,
    secondTermLessonActivities: List<LessonActivity>,
    onIncreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
    onDecreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        val schoolYear = lesson.schoolClass.schoolYear

        termHeader(schoolYear.firstTerm.name)
        lessonActivities(
            lessonActivities = firstTermLessonActivities,
            isFirstTerm = true,
            onIncreaseLessonActivity = onIncreaseLessonActivity,
            onDecreaseLessonActivity = onDecreaseLessonActivity,
        )

        termHeader(schoolYear.secondTerm.name)
        lessonActivities(
            lessonActivities = secondTermLessonActivities,
            isFirstTerm = false,
            onIncreaseLessonActivity = onIncreaseLessonActivity,
            onDecreaseLessonActivity = onDecreaseLessonActivity,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.termHeader(termName: String) {
    stickyHeader {
        Surface(modifier = Modifier.fillParentMaxWidth()) {
            Text(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                style = MaterialTheme.typography.headlineSmall,
                text = stringResource(R.string.lesson_term, termName),
            )
        }
    }
}

private fun LazyListScope.lessonActivities(
    lessonActivities: List<LessonActivity>,
    isFirstTerm: Boolean,
    onIncreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
    onDecreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
) {
    items(
        lessonActivities,
        key = { lessonActivity ->
            "${lessonActivity.student.id}-${if (isFirstTerm) "0" else "1"}"
        },
    ) { lessonActivity ->
        LessonActivityItem(
            studentFullName = lessonActivity.student.fullName,
            lessonActivitySum = lessonActivity.sum,
            onIncreaseLessonActivity = { onIncreaseLessonActivity(lessonActivity) },
            onDecreaseLessonActivity = { onDecreaseLessonActivity(lessonActivity) },
        )
    }
}

@Composable
private fun LessonActivityItem(
    studentFullName: String,
    lessonActivitySum: Long?,
    onIncreaseLessonActivity: () -> Unit,
    onDecreaseLessonActivity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(studentFullName) },
        supportingContent = { LessonActivitySum(sum = lessonActivitySum) },
        leadingContent = {
            TeacherIconButton(action = TeacherActions.minus(onClick = onDecreaseLessonActivity))
        },
        trailingContent = {
            TeacherIconButton(action = TeacherActions.plus(onClick = onIncreaseLessonActivity))
        }
    )
}

@Composable
private fun LessonActivitySum(
    sum: Long?,
    modifier: Modifier = Modifier,
) {
    val sumString =
        if (sum != null && sum != 0L) sum else stringResource(R.string.lesson_no_activity)
    Text(modifier = modifier, text = "($sumString)")
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TeacherLargeText(stringResource(R.string.lesson_no_students_in_class))
    }
}

@Preview
@Composable
private fun ActivityScreenPreview(
    @PreviewParameter(
        LessonActivityUiStatePreviewParameterProvider::class,
        limit = 5,
    ) lessonActivityUiState: LessonActivityUiState
) {
    TeacherTheme {
        Surface {
            val lesson = remember { LessonPreviewParameterProvider().values.first() }

            LessonActivityScreen(
                lessonActivityUiStateResult = Result.Success(lessonActivityUiState),
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                onIncreaseLessonActivity = {},
                onDecreaseLessonActivity = {},
            )
        }
    }
}