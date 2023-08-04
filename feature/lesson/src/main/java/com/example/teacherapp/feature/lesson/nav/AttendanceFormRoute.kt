package com.example.teacherapp.feature.lesson.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.lesson.attendance.AttendanceFormScreen
import com.example.teacherapp.feature.lesson.attendance.data.AttendanceFormViewModel

@Composable
internal fun AttendanceFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    viewModel: AttendanceFormViewModel = hiltViewModel(),
) {
    val lessonScheduleResult by viewModel.lessonScheduleResult.collectAsStateWithLifecycle()
    val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    AttendanceFormScreen(
        lessonScheduleResult = lessonScheduleResult,
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