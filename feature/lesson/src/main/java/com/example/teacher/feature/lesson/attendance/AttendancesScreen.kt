package com.example.teacher.feature.lesson.attendance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.LessonEventAttendance
import com.example.teacher.core.model.data.StudentWithAttendance
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.LessonEventAttendancesPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.R
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun AttendancesScreen(
    scheduleAttendancesResult: Result<List<LessonEventAttendance>>,
    studentsWithAttendanceResult: Result<List<StudentWithAttendance>>,
    snackbarHostState: SnackbarHostState,
    onScheduleAttendanceClick: (id: Long) -> Unit,
    onAddScheduleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = { TeacherFab(TeacherActions.add(onAddScheduleClick)) },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier.padding(innerPadding),
            result = scheduleAttendancesResult,
        ) { scheduleAttendances ->
            var showStatisticsDialog by rememberSaveable { mutableStateOf(false) }
            val onDismissStatisticDialogRequest = { showStatisticsDialog = false }

            MainContent(
                modifier = Modifier.fillMaxSize(),
                scheduleAttendances = scheduleAttendances,
                onScheduleAttendanceClick = onScheduleAttendanceClick,
                onShowStatisticsClick = { showStatisticsDialog = true },
            )

            if (showStatisticsDialog) {
                AttendanceStatisticsDialog(
                    studentsWithAttendanceResult = studentsWithAttendanceResult,
                    onDismissRequest = onDismissStatisticDialogRequest,
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    scheduleAttendances: List<LessonEventAttendance>,
    onScheduleAttendanceClick: (id: Long) -> Unit,
    onShowStatisticsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (scheduleAttendances.isEmpty()) {
        TeacherLargeText(
            modifier = modifier.padding(MaterialTheme.spacing.small),
            text = stringResource(R.string.lesson_attendance_no_schedule),
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        item {
            TeacherButton(
                modifier = Modifier.fillParentMaxWidth(),
                label = stringResource(R.string.lesson_attendance_statistics),
                icon = TeacherIcons.chart(),
                onClick = onShowStatisticsClick,
            )

            Spacer(Modifier.padding(MaterialTheme.spacing.small))
        }

        items(scheduleAttendances, key = { it.eventId }) { scheduleAttendance ->
            AttendanceItem(
                date = scheduleAttendance.date,
                startTime = scheduleAttendance.startTime,
                endTime = scheduleAttendance.endTime,
                presentCount = scheduleAttendance.presentCount,
                lateCount = scheduleAttendance.lateCount,
                absentCount = scheduleAttendance.absentCount,
                excusedAbsenceCount = scheduleAttendance.excusedAbsenceCount,
                exemptionCount = scheduleAttendance.exemptionCount,
                attendanceNotSetCount = scheduleAttendance.attendanceNotSetCount,
                onClick = { onScheduleAttendanceClick(scheduleAttendance.eventId) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AttendanceItem(
    date: LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
    presentCount: Long,
    lateCount: Long,
    absentCount: Long,
    excusedAbsenceCount: Long,
    exemptionCount: Long,
    attendanceNotSetCount: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        overlineContent = { Text(text = TimeUtils.format(date)) },
        headlineContent = { Text(text = TimeUtils.format(startTime, endTime)) },
        supportingContent = {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            ) {
                AttendanceCountItem(
                    label = stringResource(R.string.lesson_present_short),
                    count = presentCount,
                )
                AttendanceCountItem(
                    label = stringResource(R.string.lesson_late_short),
                    count = lateCount,
                )
                AttendanceCountItem(
                    label = stringResource(R.string.lesson_absent_short),
                    count = absentCount,
                )
                AttendanceCountItem(
                    label = stringResource(R.string.lesson_excused_absence_short),
                    count = excusedAbsenceCount,
                )
                AttendanceCountItem(
                    label = stringResource(R.string.lesson_exemption_short),
                    count = exemptionCount,
                )
                AttendanceCountItem(
                    label = stringResource(R.string.lesson_no_attendance_short),
                    count = attendanceNotSetCount,
                    emphasis = true,
                )
            }
        },
    )
}

@Composable
private fun AttendanceCountItem(
    label: String,
    count: Long,
    modifier: Modifier = Modifier,
    emphasis: Boolean = false,
) {
    if (count <= 0) {
        return
    }

    Card(
        modifier = modifier,
        colors = if (emphasis) CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) else CardDefaults.cardColors(),
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            text = "$label ($count)",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
private fun AttendancesScreenPreview(
    @PreviewParameter(
        LessonEventAttendancesPreviewParameterProvider::class
    ) data: List<LessonEventAttendance>
) {
    TeacherTheme {
        Surface {
            AttendancesScreen(
                scheduleAttendancesResult = Result.Success(data),
                studentsWithAttendanceResult = Result.Loading,
                snackbarHostState = remember { SnackbarHostState() },
                onScheduleAttendanceClick = {},
                onAddScheduleClick = {},
            )
        }
    }
}