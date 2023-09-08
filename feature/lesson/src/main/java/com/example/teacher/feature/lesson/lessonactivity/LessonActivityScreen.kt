package com.example.teacher.feature.lesson.lessonactivity

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.LessonActivity
import com.example.teacher.core.ui.component.TeacherIconButton
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.LessonActivitiesPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.R

@Composable
internal fun LessonActivityScreen(
    lessonActivitiesResult: Result<List<LessonActivity>>,
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
            result = lessonActivitiesResult,
        ) { lessonActivities ->
            MainContent(
                modifier = Modifier.fillMaxSize(),
                lessonActivities = lessonActivities,
                onIncreaseLessonActivity = onIncreaseLessonActivity,
                onDecreaseLessonActivity = onDecreaseLessonActivity,
            )
        }
    }
}

@Composable
private fun MainContent(
    lessonActivities: List<LessonActivity>,
    onIncreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
    onDecreaseLessonActivity: (lessonActivity: LessonActivity) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        items(
            lessonActivities,
            key = { lessonActivity -> lessonActivity.student.id },
        ) { lessonActivity ->
            LessonActivityItem(
                studentFullName = lessonActivity.student.fullName,
                lessonActivitySum = lessonActivity.sum,
                onIncreaseLessonActivity = { onIncreaseLessonActivity(lessonActivity) },
                onDecreaseLessonActivity = { onDecreaseLessonActivity(lessonActivity) },
            )
        }

        if (lessonActivities.isEmpty()) {
            item {
                TeacherLargeText(stringResource(R.string.lesson_no_students_in_class))
            }
        }
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

@Preview
@Composable
private fun ActivityScreenPreview(
    @PreviewParameter(
        LessonActivitiesPreviewParameterProvider::class,
        limit = 1,
    ) lessonActivities: List<LessonActivity>
) {
    TeacherTheme {
        Surface {
            LessonActivityScreen(
                lessonActivitiesResult = Result.Success(lessonActivities),
                snackbarHostState = remember { SnackbarHostState() },
                onIncreaseLessonActivity = {},
                onDecreaseLessonActivity = {},
            )
        }
    }
}