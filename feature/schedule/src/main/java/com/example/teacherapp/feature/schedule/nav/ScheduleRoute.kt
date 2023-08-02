package com.example.teacherapp.feature.schedule.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.schedule.ScheduleScreen
import com.example.teacherapp.feature.schedule.data.ScheduleViewModel

@Composable
internal fun ScheduleRoute(
    onAddScheduleClick: () -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val lessonSchedulesResult by viewModel.lessonSchedulesResult.collectAsStateWithLifecycle()
    val date by viewModel.date.collectAsStateWithLifecycle()

    ScheduleScreen(
        lessonSchedulesResult = lessonSchedulesResult,
        date = date,
        onDateSelected = viewModel::onDateSelected,
        onPrevDateClick = viewModel::onPrevDateClick,
        onNextDateClick = viewModel::onNextDateClick,
        onScheduleClick = {}, // TODO: Handle click.
        onAddScheduleClick = onAddScheduleClick,
    )
}