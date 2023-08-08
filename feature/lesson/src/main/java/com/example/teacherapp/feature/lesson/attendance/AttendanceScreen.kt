package com.example.teacherapp.feature.lesson.attendance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.ui.component.TeacherRadioButton
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.EventsPreviewParameterProvider
import com.example.teacherapp.core.ui.paramprovider.LessonAttendancesPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AttendanceScreen(
    eventResult: Result<Event>,
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
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TeacherTopBar(
                title = "Wystawienie frekwencji",
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
                    attendance = selectedAttendance,
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
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        if (eventResult is Result.Success) {
            item {
                val event = eventResult.data
                val lessonData =
                    event.lesson?.let { lesson -> "${lesson.name} ${lesson.schoolClass.name}" }
                val startTime = event.startTime
                val endTime = event.endTime
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                ) {
                    if (lessonData != null) {
                        Text(lessonData)
                    }
                    Text(TimeUtils.format(event.date))
                    Text(TimeUtils.format(startTime, endTime))
                }
            }
        }

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
    }
}

@Composable
private fun AttendanceDialog(
    studentFullName: String,
    attendance: Attendance?,
    onAttendanceSelect: (attendance: Attendance?) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text("Wystaw frekwencję") },
        text = {
            Column {
                Text(studentFullName)
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .selectableGroup(),
                ) {
                    TeacherRadioButton(
                        label = "obecny",
                        selected = attendance == Attendance.Present,
                        onClick = { onAttendanceSelect(Attendance.Present) },
                    )
                    TeacherRadioButton(
                        label = "spóźniony",
                        selected = attendance == Attendance.Late,
                        onClick = { onAttendanceSelect(Attendance.Late) },
                    )
                    TeacherRadioButton(
                        label = "nieobecny",
                        selected = attendance == Attendance.Absent,
                        onClick = { onAttendanceSelect(Attendance.Absent) },
                    )
                    TeacherRadioButton(
                        label = "nieobecność usprawiedliwiona",
                        selected = attendance == Attendance.ExcusedAbsence,
                        onClick = { onAttendanceSelect(Attendance.ExcusedAbsence) },
                    )
                    TeacherRadioButton(
                        label = "zwolnienie",
                        selected = attendance == Attendance.Exemption,
                        onClick = { onAttendanceSelect(Attendance.Exemption) },
                    )
                    TeacherRadioButton(
                        label = "brak",
                        selected = attendance == null,
                        onClick = { onAttendanceSelect(null) },
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Anuluj")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick()
                onDismissRequest()
            }) {
                Text("Ok")
            }
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
    val attendanceText = remember(attendance) {
        when (attendance) {
            Attendance.Present -> "obecny"
            Attendance.Late -> "spóźniony"
            Attendance.Absent -> "nieobecny"
            Attendance.ExcusedAbsence -> "nieobecność usprawiedliwiona"
            Attendance.Exemption -> "zwolnienie"
            null -> "brak"
        }
    }

    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = { Text(studentFullName) },
        supportingContent = { Text(attendanceText) },
    )
}

@Preview
@Composable
private fun AttendanceScreenPreview(
    @PreviewParameter(
        LessonAttendancesPreviewParameterProvider::class
    ) lessonAttendances: List<LessonAttendance>
) {
    TeacherAppTheme {
        Surface {
            val event = remember {
                EventsPreviewParameterProvider().values.first().first()
            }
            var showDialog by remember { mutableStateOf(false) }
            var selectedStudentFullName by remember { mutableStateOf("") }
            var selectedAttendance by remember { mutableStateOf<Attendance?>(null) }

            AttendanceScreen(
                eventResult = Result.Success(event),
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
                onAttendanceConfirmClick = {},
            )
        }
    }
}