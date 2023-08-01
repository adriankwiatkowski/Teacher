package com.example.teacherapp.feature.schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.LessonSchedule
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.LessonSchedulesPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@Composable
internal fun ScheduleScreen(
    lessonSchedulesResult: Result<List<LessonSchedule>>,
    onAddScheduleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier.padding(MaterialTheme.spacing.small),
        result = lessonSchedulesResult,
    ) { lessonSchedules ->
        MainContent(
            modifier = modifier.fillMaxSize(),
            lessonSchedules = lessonSchedules,
            onAddScheduleClick = onAddScheduleClick,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainContent(
    lessonSchedules: List<LessonSchedule>,
    onAddScheduleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        stickyHeader {
            TeacherButton(onClick = onAddScheduleClick) {
                Text(text = "Dodaj termin zajęć")
            }
        }

        items(lessonSchedules, key = { it.id }) { schedule ->
            ListItem(
                overlineContent = { Text(TimeUtils.format(schedule.date)) },
                headlineContent = {
                    val lessonName = schedule.lesson.name
                    val schoolClassName = schedule.lesson.schoolClass.name
                    Text("$lessonName $schoolClassName")
                },
                supportingContent = {
                    val startTime = TimeUtils.format(schedule.startTime)
                    val endTime = TimeUtils.format(schedule.endTime)
                    Text("$startTime - $endTime")
                },
            )
        }
    }
}

@Preview
@Composable
private fun ScheduleScreenPreview(
    @PreviewParameter(
        LessonSchedulesPreviewParameterProvider::class
    ) lessonSchedules: List<LessonSchedule>
) {
    TeacherAppTheme {
        Surface {
            ScheduleScreen(
                lessonSchedulesResult = Result.Success(lessonSchedules),
                onAddScheduleClick = {},
            )
        }
    }
}