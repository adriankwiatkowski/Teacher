package com.example.teacher.feature.schedule.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun DateForm(
    title: String,
    day: DayOfWeek,
    onDayChange: (day: DayOfWeek) -> Unit,
    date: LocalDate,
    onDateChange: (date: LocalDate) -> Unit,
    startTime: LocalTime,
    onStartTimeChange: (date: LocalTime) -> Unit,
    endTime: LocalTime,
    onEndTimeChange: (date: LocalTime) -> Unit,
    showTermPicker: Boolean,
    showDayPicker: Boolean,
    showTypeControls: Boolean,
    isFirstTermSelected: Boolean,
    onTermSelected: (isFirstTermSelected: Boolean) -> Unit,
    type: EventType,
    onTypeChange: (type: EventType) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .animateContentSize(),
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            if (showTermPicker) {
                Text(text = "Semestr", style = MaterialTheme.typography.labelLarge)
                Column(Modifier.selectableGroup()) {
                    TeacherRadioButton(
                        label = "Pierwszy semestr",
                        selected = isFirstTermSelected,
                        onClick = { onTermSelected(true) },
                    )
                    TeacherRadioButton(
                        label = "Drugi semestr",
                        selected = !isFirstTermSelected,
                        onClick = { onTermSelected(false) },
                    )
                }
                Divider()
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }

            if (showDayPicker) {
                LessonDayPicker(day = day, onDaySelected = onDayChange)
            } else {
                LessonDatePicker(date = date, onDateSelected = onDateChange)
            }

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

            if (showTypeControls) {
                Divider()
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                Text(text = "Typ", style = MaterialTheme.typography.labelLarge)
                EventTypeControls(type = type, onTypeChange = onTypeChange)
            }
        }
    }
}

@Preview
@Composable
private fun DateFormPreview() {
    TeacherTheme {
        Surface {
            var day by remember { mutableStateOf(TimeUtils.monday()) }
            var date by remember { mutableStateOf(TimeUtils.currentDate()) }
            var startTime by remember { mutableStateOf(TimeUtils.localTimeOf(8, 0)) }
            var endTime by remember { mutableStateOf(TimeUtils.localTimeOf(8, 45)) }
            var isFirstTermSelected by remember { mutableStateOf(true) }
            var type by remember { mutableStateOf(EventType.Weekly) }

            DateForm(
                title = "Termin zajęć",
                day = day,
                onDayChange = { day = it },
                date = date,
                onDateChange = { date = it },
                startTime = startTime,
                onStartTimeChange = { startTime = it },
                endTime = endTime,
                onEndTimeChange = { endTime = it },
                showTermPicker = type == EventType.Weekly || type == EventType.EveryTwoWeeks,
                showDayPicker = type in setOf(EventType.Weekly, EventType.EveryTwoWeeks),
                isFirstTermSelected = isFirstTermSelected,
                onTermSelected = { isFirstTermSelected = it },
                showTypeControls = true,
                type = type,
                onTypeChange = { type = it },
            )
        }
    }
}