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
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.ui.component.TeacherFab
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.icon.TeacherIcons
import com.example.teacherapp.core.ui.paramprovider.EventsPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate

@Composable
internal fun ScheduleScreen(
    eventsResult: Result<List<Event>>,
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
            result = eventsResult,
        ) { events ->
            MainContent(
                modifier = Modifier.fillMaxSize(),
                events = events,
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
    events: List<Event>,
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

        if (events.isEmpty()) {
            item {
                EmptyState()
            }
        } else {
            events(
                events = events,
                onScheduleClick = onScheduleClick,
            )
        }
    }
}

private fun LazyListScope.events(
    events: List<Event>,
    onScheduleClick: (id: Long) -> Unit,
) {
    items(events, key = { it.id }) { event ->
        EventItem(
            event = event,
            onClick = { onScheduleClick(event.id) },
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
private fun EventItem(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(TimeUtils.format(event.startTime, event.endTime))
        },
        supportingContent = {
            val lessonName = event.lesson.name
            val schoolClassName = event.lesson.schoolClass.name
            Text("$lessonName $schoolClassName")
        },
    )
}

@Preview
@Composable
private fun ScheduleScreenPreview(
    @PreviewParameter(
        EventsPreviewParameterProvider::class
    ) events: List<Event>
) {
    TeacherAppTheme {
        Surface {
            ScheduleScreen(
                eventsResult = Result.Success(events),
                date = events.first().date,
                onDateSelected = {},
                onPrevDateClick = {},
                onNextDateClick = {},
                onScheduleClick = {},
                onAddScheduleClick = {},
            )
        }
    }
}