package com.example.teacher.feature.lesson.attendance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.LessonAttendance
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.component.TeacherTextButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.EventsPreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.LessonAttendancesPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AttendanceScreen(
    eventResult: Result<Event>,
    snackbarHostState: SnackbarHostState,
    lessonAttendancesResult: Result<List<LessonAttendance>>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onLessonAttendanceClick: (lessonAttendance: LessonAttendance) -> Unit,
    showAttendanceDialog: Boolean,
    selectedStudentFullName: String,
    selectedAttendance: Attendance?,
    onAttendanceSelect: (attendance: Attendance?) -> Unit,
    onAttendanceDismissRequest: () -> Unit,
    onAttendanceConfirmClick: () -> Unit,
    onEventEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = stringResource(R.string.lesson_attendance_title),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (showAttendanceDialog) {
                AttendanceDialog(
                    studentFullName = selectedStudentFullName,
                    selectedAttendance = selectedAttendance,
                    onAttendanceSelect = onAttendanceSelect,
                    onDismissRequest = onAttendanceDismissRequest,
                    onConfirmClick = onAttendanceConfirmClick,
                )
            }

            ResultContent(result = lessonAttendancesResult) { lessonAttendances ->
                MainContent(
                    eventResult = eventResult,
                    lessonAttendances = lessonAttendances,
                    onLessonAttendanceClick = onLessonAttendanceClick,
                    onEventEditClick = onEventEditClick,
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    eventResult: Result<Event>,
    lessonAttendances: List<LessonAttendance>,
    onLessonAttendanceClick: (lessonAttendance: LessonAttendance) -> Unit,
    onEventEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        header(eventResult = eventResult, onEventEditClick = onEventEditClick)

        items(
            lessonAttendances,
            key = { attendance -> attendance.student.id },
        ) { attendance ->
            AttendanceItem(
                studentFullName = attendance.student.fullName,
                attendance = attendance.attendance,
                onClick = { onLessonAttendanceClick(attendance) },
            )
        }

        if (lessonAttendances.isEmpty()) {
            item {
                TeacherLargeText(stringResource(R.string.lesson_no_students_in_class))
            }
        }
    }
}

private fun LazyListScope.header(
    eventResult: Result<Event>,
    onEventEditClick: () -> Unit,
) {
    if (eventResult is Result.Success) {
        item {
            val event = eventResult.data
            val lessonNameWithClass =
                event.lesson?.let { lesson -> "${lesson.name} ${lesson.schoolClass.name}" }
            val startTime = event.startTime
            val endTime = event.endTime

            Card(Modifier.fillParentMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                ) {
                    if (event.isCancelled) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = stringResource(R.string.lesson_attendance_cancelled),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    if (lessonNameWithClass != null) {
                        Text(
                            text = lessonNameWithClass,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    Text(TimeUtils.getDisplayNameOfDayOfWeek(event.date).capitalize(Locale.current))
                    Text(stringResource(R.string.lesson_date, TimeUtils.format(event.date)))
                    Text(stringResource(R.string.lesson_time, TimeUtils.format(startTime, endTime)))

                    TeacherButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.lesson_attendance_edit_event),
                        onClick = onEventEditClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun AttendanceDialog(
    studentFullName: String,
    selectedAttendance: Attendance?,
    onAttendanceSelect: (attendance: Attendance?) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.lesson_set_attendance)) },
        text = {
            Column {
                Text(studentFullName)
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .selectableGroup(),
                ) {
                    val attendances = remember {
                        listOf(
                            Attendance.Present,
                            Attendance.Late,
                            Attendance.Absent,
                            Attendance.ExcusedAbsence,
                            Attendance.Exemption,
                            null,
                        )
                    }

                    for (attendance in attendances) {
                        TeacherRadioButton(
                            label = getAttendanceText(attendance),
                            selected = selectedAttendance == attendance,
                            onClick = { onAttendanceSelect(attendance) },
                        )
                    }
                }
            }
        },
        dismissButton = {
            TeacherTextButton(
                label = stringResource(com.example.teacher.core.ui.R.string.ui_cancel),
                onClick = onDismissRequest,
            )
        },
        confirmButton = {
            TeacherTextButton(
                label = stringResource(com.example.teacher.core.ui.R.string.ui_ok),
                onClick = {
                    onConfirmClick()
                    onDismissRequest()
                },
            )
        },
    )
}

@Composable
private fun AttendanceItem(
    studentFullName: String,
    attendance: Attendance?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val attendanceText = getAttendanceText(attendance)

    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = { Text(studentFullName) },
        supportingContent = { Text(attendanceText) },
    )
}

@Composable
private fun getAttendanceText(attendance: Attendance?): String {
    return when (attendance) {
        Attendance.Present -> stringResource(R.string.lesson_present)
        Attendance.Late -> stringResource(R.string.lesson_late)
        Attendance.Absent -> stringResource(R.string.lesson_absent)
        Attendance.ExcusedAbsence -> stringResource(R.string.lesson_excused_absence)
        Attendance.Exemption -> stringResource(R.string.lesson_exemption)
        null -> stringResource(R.string.lesson_no_attendance)
    }
}

@Preview
@Composable
private fun AttendanceScreenPreview(
    @PreviewParameter(
        LessonAttendancesPreviewParameterProvider::class
    ) lessonAttendances: List<LessonAttendance>
) {
    TeacherTheme {
        Surface {
            val event = remember {
                EventsPreviewParameterProvider().values.first().first()
            }
            var showDialog by remember { mutableStateOf(false) }
            var selectedStudentFullName by remember { mutableStateOf("") }
            var selectedAttendance by remember { mutableStateOf<Attendance?>(null) }

            AttendanceScreen(
                eventResult = Result.Success(event),
                snackbarHostState = remember { SnackbarHostState() },
                lessonAttendancesResult = Result.Success(lessonAttendances),
                showNavigationIcon = true,
                onNavBack = {},
                onLessonAttendanceClick = { lessonAttendance ->
                    selectedStudentFullName = lessonAttendance.student.fullName
                    selectedAttendance = lessonAttendance.attendance
                    showDialog = true
                },
                showAttendanceDialog = showDialog,
                selectedStudentFullName = selectedStudentFullName,
                selectedAttendance = selectedAttendance,
                onAttendanceSelect = { selectedAttendance = it },
                onAttendanceDismissRequest = { showDialog = false },
                onEventEditClick = {},
                onAttendanceConfirmClick = {},
            )
        }
    }
}