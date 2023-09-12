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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schedule.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun DateForm(
    title: String,
    lesson: Lesson?,
    onLessonPickerClick: () -> Unit,
    firstTermName: String,
    secondTermName: String,
    day: DayOfWeek,
    onDayChange: (day: DayOfWeek) -> Unit,
    date: LocalDate,
    onDateChange: (date: LocalDate) -> Unit,
    startTime: LocalTime,
    onStartTimeChange: (date: LocalTime) -> Unit,
    endTime: LocalTime,
    onEndTimeChange: (date: LocalTime) -> Unit,
    showLessonPicker: Boolean,
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

            if (showLessonPicker) {
                val text = if (lesson != null) {
                    val lessonName = lesson.name
                    val schoolClassName = lesson.schoolClass.name
                    "$lessonName $schoolClassName"
                } else {
                    stringResource(R.string.schedule_pick_lesson)
                }

                Text(
                    text = stringResource(R.string.schedule_lesson),
                    style = MaterialTheme.typography.labelMedium,
                )
                Column(Modifier.padding(MaterialTheme.spacing.small)) {
                    if (lesson != null) {
                        Text(
                            text = lesson.schoolClass.schoolYear.name,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }

                    TeacherButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = text,
                        onClick = onLessonPickerClick,
                    )

                    if (lesson == null) {
                        Text(
                            text = stringResource(R.string.schedule_lesson_not_selected),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                Divider()
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }

            if (showTermPicker) {
                Text(
                    text = stringResource(R.string.schedule_term),
                    style = MaterialTheme.typography.labelLarge,
                )
                Column(Modifier.selectableGroup()) {
                    TeacherRadioButton(
                        label = stringResource(R.string.schedule_term_with_data, firstTermName),
                        selected = isFirstTermSelected,
                        onClick = { onTermSelected(true) },
                    )
                    TeacherRadioButton(
                        label = stringResource(R.string.schedule_term_with_data, secondTermName),
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
                label = stringResource(R.string.schedule_start_time),
                time = startTime,
                onTimeSelected = onStartTimeChange,
            )
            LessonTimePicker(
                label = stringResource(R.string.schedule_end_time),
                time = endTime,
                onTimeSelected = onEndTimeChange,
            )

            if (showTypeControls) {
                Divider()
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                Text(
                    text = stringResource(R.string.schedule_type),
                    style = MaterialTheme.typography.labelLarge,
                )
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
                lesson = null,
                onLessonPickerClick = {},
                firstTermName = "I",
                secondTermName = "II",
                day = day,
                onDayChange = { day = it },
                date = date,
                onDateChange = { date = it },
                startTime = startTime,
                onStartTimeChange = { startTime = it },
                endTime = endTime,
                onEndTimeChange = { endTime = it },
                showLessonPicker = true,
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