package com.example.teacherapp.ui.screens.other.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.LessonCalendar
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.picker.TeacherTimePicker
import com.example.teacherapp.core.ui.paramprovider.LessonCalendarsPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun LessonPlanningScreen(
    lessonCalendars: List<LessonCalendar>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.small)
    ) {
        item {
            Header(
                schoolClassName = "3A",
                lessonName = "Matematyka",
            )
        }

        // TODO: Separate form when adding and editing lesson date, and only one form should be present.
        dateForms(lessonCalendars)
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }

        item {
            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
            ) {
                Text(text = "Dodaj termin zajęć")
            }
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
private fun LazyListScope.dateForms(
    lessonCalendars: List<LessonCalendar>,
) {
    item {
        Box(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.Companion.align(Alignment.Center),
                text = "Terminy zajęć",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }

    items(lessonCalendars, key = { it.id }) { lessonCalendar ->
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
                LessonDatePicker(
                    date = lessonCalendar.date,
                    onDateSelected = { date ->
                        // TODO: Set date.
                    },
                )
                LessonTimePicker(
                    label = "Czas rozpoczęcia:",
                    time = lessonCalendar.startTime,
                    onTimeSelected = { time ->
                        // TODO: Set start time.
                    },
                )
                LessonTimePicker(
                    label = "Czas zakończenia:",
                    time = lessonCalendar.endTime,
                    onTimeSelected = { time ->
                        // TODO: Set end time.
                    },
                )

                FlowRow {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = true, onClick = {})
                        Text("Jednorazowe")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = false, onClick = {})
                        Text("Cotygodniowe")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = false, onClick = {})
                        Text("Co 2 tygodnie")
                    }
                }
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
private fun LessonPlanningScreenPreview(
    @PreviewParameter(
        LessonCalendarsPreviewParameterProvider::class
    ) lessonCalendars: List<LessonCalendar>
) {
    TeacherAppTheme {
        Surface {
            LessonPlanningScreen(
                lessonCalendars = lessonCalendars,
            )
        }
    }
}