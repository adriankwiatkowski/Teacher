package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.feature.lesson.attendance.AttendanceScreen
import com.example.teacher.feature.lesson.attendance.data.AttendanceViewModel

@Composable
internal fun AttendanceRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onEventEditClick: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel(),
) {
    val eventResult by viewModel.eventResult.collectAsStateWithLifecycle()
    val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    AttendanceScreen(
        eventResult = eventResult,
        snackbarHostState = snackbarHostState,
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
        onEventEditClick = onEventEditClick,
    )
}