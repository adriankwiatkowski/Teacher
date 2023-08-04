package com.example.teacherapp.feature.lesson.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.lesson.attendance.AttendanceScreen
import com.example.teacherapp.feature.lesson.attendance.data.AttendanceViewModel

@Composable
internal fun AttendanceRoute(
    onScheduleAttendanceClick: (lessonScheduleId: Long) -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel(),
) {
    val attendancesResult by viewModel.attendancesResult.collectAsStateWithLifecycle()

    AttendanceScreen(
        scheduleAttendancesResult = attendancesResult,
        onScheduleAttendanceClick = onScheduleAttendanceClick,
    )
}