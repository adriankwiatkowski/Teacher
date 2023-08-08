package com.example.teacherapp.feature.lesson.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.lesson.attendance.AttendancesScreen
import com.example.teacherapp.feature.lesson.attendance.data.AttendancesViewModel

@Composable
internal fun AttendancesRoute(
    onScheduleAttendanceClick: (lessonScheduleId: Long) -> Unit,
    viewModel: AttendancesViewModel = hiltViewModel(),
) {
    val attendancesResult by viewModel.attendancesResult.collectAsStateWithLifecycle()

    AttendancesScreen(
        scheduleAttendancesResult = attendancesResult,
        onScheduleAttendanceClick = onScheduleAttendanceClick,
    )
}