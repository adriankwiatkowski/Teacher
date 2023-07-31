package com.example.teacherapp.ui.screens.other.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherRadioButton
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.picker.TeacherTimePicker
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun LessonPlanningScreen(
    schoolClassName: String,
    lessonName: String,
    lessonCalendarForm: LessonCalendarForm,
    onDateChange: (date: LocalDate) -> Unit,
    onStartTimeChange: (date: LocalTime) -> Unit,
    onEndTimeChange: (date: LocalTime) -> Unit,
    onTypeChange: (type: LessonCalendarFormType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Header(
            schoolClassName = schoolClassName,
            lessonName = lessonName,
        )

        DateForm(
            date = lessonCalendarForm.date,
            onDateChange = onDateChange,
            startTime = lessonCalendarForm.startTime,
            onStartTimeChange = onStartTimeChange,
            endTime = lessonCalendarForm.endTime,
            onEndTimeChange = onEndTimeChange,
            type = lessonCalendarForm.type,
            onTypeChange = onTypeChange,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
        ) {
            Text(text = "Dodaj termin zajęć")
        }
    }
}

@Composable
private fun Header(
    schoolClassName: String,
    lessonName: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = "Dodaj termin zajęć",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = "$lessonName $schoolClassName",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DateForm(
    date: LocalDate,
    onDateChange: (date: LocalDate) -> Unit,
    startTime: LocalTime,
    onStartTimeChange: (date: LocalTime) -> Unit,
    endTime: LocalTime,
    onEndTimeChange: (date: LocalTime) -> Unit,
    type: LessonCalendarFormType,
    onTypeChange: (type: LessonCalendarFormType) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small),
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.Companion.align(Alignment.Center),
                    text = "Termin zajęć",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            LessonDatePicker(date = date, onDateSelected = onDateChange)
            LessonTimePicker(
                label = "Czas rozpoczęcia:",
                time = startTime,
                onTimeSelected = onStartTimeChange,
            )
            LessonTimePicker(
                label = "Czas zakończenia:",
                time = endTime,
                onTimeSelected = onEndTimeChange,
            )

            Column {
                TeacherRadioButton(
                    label = "Jednorazowe",
                    selected = type == LessonCalendarFormType.Once,
                    onClick = { onTypeChange(LessonCalendarFormType.Once) },
                )
                TeacherRadioButton(
                    label = "Cotygodniowe",
                    selected = type == LessonCalendarFormType.Weekly,
                    onClick = { onTypeChange(LessonCalendarFormType.Weekly) },
                )
                TeacherRadioButton(
                    label = "Co 2 tygodnie",
                    selected = type == LessonCalendarFormType.EveryTwoWeeks,
                    onClick = { onTypeChange(LessonCalendarFormType.EveryTwoWeeks) },
                )
            }
        }
    }
}

@Composable
private fun LessonDatePicker(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    Column {
        Text(
            text = TimeUtils.getDisplayNameOfDayOfWeek(date),
            style = MaterialTheme.typography.labelMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Data:")
            Text(text = TimeUtils.format(date))
            TeacherDatePicker(
                date = date,
                onDateSelected = onDateSelected,
                label = { Text("Wybierz datę") },
            )
        }
    }
}

@Composable
private fun LessonTimePicker(
    label: String,
    time: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)
        Text(text = TimeUtils.format(time))
        TeacherTimePicker(
            time = time,
            onTimeSelected = onTimeSelected,
            label = { Text("Wybierz godzinę") },
        )
    }
}

@Preview
@Composable
private fun LessonPlanningScreenPreview() {
    TeacherAppTheme {
        Surface {
            var calendarForm by remember {
                mutableStateOf(
                    LessonCalendarForm(
                        date = TimeUtils.currentDate(),
                        startTime = TimeUtils.localTimeOf(8, 0),
                        endTime = TimeUtils.localTimeOf(8, 45),
                        type = LessonCalendarFormType.Weekly,
                    )
                )
            }

            LessonPlanningScreen(
                schoolClassName = "1A",
                lessonName = "Matematyka",
                lessonCalendarForm = calendarForm,
                onDateChange = { calendarForm = calendarForm.copy(date = it) },
                onStartTimeChange = { calendarForm = calendarForm.copy(startTime = it) },
                onEndTimeChange = { calendarForm = calendarForm.copy(endTime = it) },
                onTypeChange = { calendarForm = calendarForm.copy(type = it) }
            )
        }
    }
}

internal data class LessonCalendarForm(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val type: LessonCalendarFormType,
)

internal enum class LessonCalendarFormType {
    Once, Weekly, EveryTwoWeeks
}