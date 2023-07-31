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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherTextField
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.picker.TeacherTimePicker
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun LessonPlanningScreen() {
    val lessonDate by remember { mutableStateOf(TimeUtils.currentDate()) }
    val lessonTime by remember { mutableStateOf(TimeUtils.localTimeOf(9, 0)) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.small)
    ) {
        item {
            Header()
        }

        item {
            LessonNameForm()
        }

        item {
            DateForms(lessonDate, lessonTime)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }

        item {
            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
            ) {
                Text(text = "Edytuj przedmiot")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun DateForms(
    lessonDate: LocalDate,
    lessonTime: LocalTime,
) {
    var lessonDate1 = lessonDate
    var lessonTime1 = lessonTime
    Box(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.Companion.align(Alignment.Center),
            text = "Terminy zajęć",
            style = MaterialTheme.typography.headlineSmall
        )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            LessonDatePicker(
                date = lessonDate1,
                onDateSelected = { date ->
                    lessonDate1 = date
                },
            )
            LessonTimePicker(
                label = "Czas rozpoczęcia:",
                time = lessonTime1,
                onTimeSelected = { time ->
                    lessonTime1 = time
                },
            )
            LessonTimePicker(
                label = "Czas zakończenia:",
                time = lessonTime1,
                onTimeSelected = { time ->
                    lessonTime1 = time
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

    Box(Modifier.fillMaxWidth()) {
        TeacherButton(
            modifier = Modifier.Companion.align(Alignment.Center),
            onClick = {}) {
            Text("Dodaj termin zajęć")
        }
    }
}

@Composable
private fun LessonNameForm() {
    TeacherTextField(
        modifier = Modifier.fillMaxWidth(),
        label = "Przedmiot*",
        value = "Matematyka",
        onValueChange = {},
    )

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
}

@Composable
private fun Header() {
    Text(
        text = "Formularz przedmiotu",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

    Text(
        text = "Klasa 3A",
        style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
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
            LessonPlanningScreen()
        }
    }
}