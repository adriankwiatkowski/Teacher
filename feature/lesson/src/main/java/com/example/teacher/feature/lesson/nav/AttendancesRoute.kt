package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.feature.lesson.attendance.AttendancesScreen
import com.example.teacher.feature.lesson.attendance.data.AttendancesViewModel

@Composable
internal fun AttendancesRoute(
    snackbarHostState: SnackbarHostState,
    lesson: Lesson,
    onScheduleAttendanceClick: (lessonScheduleId: Long) -> Unit,
    onAddScheduleClick: () -> Unit,
    viewModel: AttendancesViewModel = hiltViewModel(),
) {
    val attendancesResult by viewModel.attendancesResult.collectAsStateWithLifecycle()
    val studentsWithAttendanceResult by viewModel.studentsWithAttendanceResult.collectAsStateWithLifecycle()

    AttendancesScreen(
        scheduleAttendancesResult = attendancesResult,
        studentsWithAttendanceResult = studentsWithAttendanceResult,
        lesson = lesson,
        snackbarHostState = snackbarHostState,
        onScheduleAttendanceClick = onScheduleAttendanceClick,
        onAddScheduleClick = onAddScheduleClick,
    )
}