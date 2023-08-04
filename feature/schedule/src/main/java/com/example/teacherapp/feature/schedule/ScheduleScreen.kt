package com.example.teacherapp.feature.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.LessonSchedule
import com.example.teacherapp.core.ui.component.TeacherFab
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.icon.TeacherIcons
import com.example.teacherapp.core.ui.paramprovider.LessonSchedulesPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate

@Composable
internal fun ScheduleScreen(
    lessonSchedulesResult: Result<List<LessonSchedule>>,
    date: LocalDate,
    onDateSelected: (date: LocalDate) -> Unit,
    onPrevDateClick: () -> Unit,
    onNextDateClick: () -> Unit,
    onScheduleClick: (id: Long) -> Unit,
    onAddScheduleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            TeacherFab(
                imageVector = TeacherIcons.Add,
                contentDescription = null,
                onClick = onAddScheduleClick,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            result = lessonSchedulesResult,
        ) { lessonSchedules ->
            MainContent(
                modifier = Modifier.fillMaxSize(),
                lessonSchedules = lessonSchedules,
                onScheduleClick = onScheduleClick,
                date = date,
                onDateSelected = onDateSelected,
                onPrevDateClick = onPrevDateClick,
                onNextDateClick = onNextDateClick,
            )
        }
    }
}

@Composable
private fun MainContent(
    lessonSchedules: List<LessonSchedule>,
    date: LocalDate,
    onDateSelected: (date: LocalDate) -> Unit,
    onPrevDateClick: () -> Unit,
    onNextDateClick: () -> Unit,
    onScheduleClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onPrevDateClick) {
                    Icon(imageVector = TeacherIcons.Previous, contentDescription = null)
                }
                TeacherDatePicker(
                    date = date,
                    onDateSelected = onDateSelected,
                    label = { Text(TimeUtils.format(date)) },
                )
                IconButton(onClick = onNextDateClick) {
                    Icon(imageVector = TeacherIcons.Next, contentDescription = null)
                }
            }
        }

        if (lessonSchedules.isEmpty()) {
            item {
                EmptyState()
            }
        } else {
            scheduleItems(
                lessonSchedules = lessonSchedules,
                onScheduleClick = onScheduleClick,
            )
        }
    }
}

private fun LazyListScope.scheduleItems(
    lessonSchedules: List<LessonSchedule>,
    onScheduleClick: (id: Long) -> Unit,
) {
    items(lessonSchedules, key = { it.id }) { schedule ->
        ScheduleItem(
            schedule = schedule,
            onClick = { onScheduleClick(schedule.id) },
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Brak zajęć",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
private fun ScheduleItem(
    schedule: LessonSchedule,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(TimeUtils.format(schedule.startTime, schedule.endTime))
        },
        supportingContent = {
            val lessonName = schedule.lesson.name
            val schoolClassName = schedule.lesson.schoolClass.name
            Text("$lessonName $schoolClassName")
        },
    )
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
                date = lessonSchedules.first().date,
                onDateSelected = {},
                onPrevDateClick = {},
                onNextDateClick = {},
                onScheduleClick = {},
                onAddScheduleClick = {},
            )
        }
    }
}