package com.example.teacherapp.feature.lesson.nav

import androidx.compose.runtime.Composable
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.feature.lesson.attendance.AttendanceScreen

@Composable
internal fun AttendanceRoute() {
    // TODO: Get actual data.
    AttendanceScreen(
        scheduleAttendancesResult = Result.Loading,
        onScheduleAttendanceClick = {},
    )
}