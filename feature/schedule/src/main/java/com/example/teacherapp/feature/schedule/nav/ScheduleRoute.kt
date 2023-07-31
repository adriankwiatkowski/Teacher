package com.example.teacherapp.feature.schedule.nav

import androidx.compose.runtime.Composable
import com.example.teacherapp.feature.schedule.ScheduleScreen

@Composable
internal fun ScheduleRoute(
    onAddScheduleClick: () -> Unit,
) {
    ScheduleScreen(
        onAddScheduleClick = onAddScheduleClick,
    )
}