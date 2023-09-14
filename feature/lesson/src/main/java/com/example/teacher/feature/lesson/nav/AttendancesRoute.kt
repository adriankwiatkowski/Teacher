package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.feature.lesson.attendance.AttendancesScreen
import com.example.teacher.feature.lesson.attendance.data.AttendancesViewModel

@Composable
internal fun AttendancesRoute(
    snackbarHostState: SnackbarHostState,
    onScheduleAttendanceClick: (lessonScheduleId: Long) -> Unit,
    onAddScheduleClick: () -> Unit,
    viewModel: AttendancesViewModel = hiltViewModel(),
) {
    val attendancesResult by viewModel.attendancesResult.collectAsStateWithLifecycle()
    val studentsWithAttendanceResult by viewModel.studentsWithAttendanceResult.collectAsStateWithLifecycle()

    AttendancesScreen(
        scheduleAttendancesResult = attendancesResult,
        studentsWithAttendanceResult = studentsWithAttendanceResult,
        snackbarHostState = snackbarHostState,
        onScheduleAttendanceClick = onScheduleAttendanceClick,
        onAddScheduleClick = onAddScheduleClick,
    )
}