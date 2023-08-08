package com.example.teacherapp.feature.lesson.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.lesson.attendance.AttendanceScreen
import com.example.teacherapp.feature.lesson.attendance.data.AttendanceViewModel

@Composable
internal fun AttendanceRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel(),
) {
    val eventResult by viewModel.eventResult.collectAsStateWithLifecycle()
    val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    AttendanceScreen(
        eventResult = eventResult,
        lessonAttendancesResult = lessonAttendancesResult,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        onLessonAttendanceClick = viewModel::onLessonAttendanceClick,
        showAttendanceDialog = dialogState != null,
        selectedStudentFullName = dialogState?.studentFullName.orEmpty(),
        selectedAttendance = dialogState?.attendance,
        onAttendanceSelect = viewModel::onAttendanceSelect,
        onAttendanceDismissRequest = viewModel::onAttendanceDismissRequest,
        onAttendanceConfirmClick = viewModel::onAttendanceConfirmClick,
    )
}