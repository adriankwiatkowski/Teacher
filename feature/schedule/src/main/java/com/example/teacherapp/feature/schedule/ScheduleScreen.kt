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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.ui.component.TeacherFab
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.result.ErrorScreen
import com.example.teacherapp.core.ui.component.result.LoadingScreen
import com.example.teacherapp.core.ui.icon.TeacherIcons
import com.example.teacherapp.core.ui.paramprovider.EventsPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate

@Composable
internal fun ScheduleScreen(
    eventsResult: Result<List<Event>>,
    snackbarHostState: SnackbarHostState,
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            TeacherFab(
                imageVector = TeacherIcons.Add,
                contentDescription = null,
                onClick = onAddScheduleClick,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        MainContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            eventsResult = eventsResult,
            onScheduleClick = onScheduleClick,
            date = date,
            onDateSelected = onDateSelected,
            onPrevDateClick = onPrevDateClick,
            onNextDateClick = onNextDateClick,
        )
    }
}

@Composable
private fun MainContent(
    eventsResult: Result<List<Event>>,
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
            Header(
                date = date,
                onDateSelected = onDateSelected,
                onPrevDateClick = onPrevDateClick,
                onNextDateClick = onNextDateClick,
            )
        }

        when (eventsResult) {
            Result.Loading -> {
                item {
                    LoadingState()
                }
            }

            is Result.Success -> {
                val events = eventsResult.data
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

            is Result.Error -> {
                item {
                    ErrorState()
                }
            }
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
private fun Header(
    date: LocalDate,
    onDateSelected: (date: LocalDate) -> Unit,
    onPrevDateClick: () -> Unit,
    onNextDateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = TimeUtils.getDisplayNameOfDayOfWeek(date),
            style = MaterialTheme.typography.headlineSmall,
        )

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
private fun LoadingState(modifier: Modifier = Modifier) {
    LoadingScreen(modifier = modifier)
}

@Composable
private fun ErrorState(modifier: Modifier = Modifier) {
    ErrorScreen(modifier = modifier)
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
            val text = event.lesson?.let { lesson -> "${lesson.name} ${lesson.schoolClass.name}" }
                ?: "Wydarzenie"
            Text(text)
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
                snackbarHostState = remember { SnackbarHostState() },
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

@Preview
@Composable
private fun ScheduleScreenLoadingPreview() {
    TeacherAppTheme {
        Surface {
            ScheduleScreen(
                eventsResult = Result.Loading,
                snackbarHostState = remember { SnackbarHostState() },
                date = TimeUtils.currentDate(),
                onDateSelected = {},
                onPrevDateClick = {},
                onNextDateClick = {},
                onScheduleClick = {},
                onAddScheduleClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ScheduleScreenErrorPreview() {
    TeacherAppTheme {
        Surface {
            ScheduleScreen(
                eventsResult = Result.Error(IllegalStateException()),
                snackbarHostState = remember { SnackbarHostState() },
                date = TimeUtils.currentDate(),
                onDateSelected = {},
                onPrevDateClick = {},
                onNextDateClick = {},
                onScheduleClick = {},
                onAddScheduleClick = {},
            )
        }
    }
}