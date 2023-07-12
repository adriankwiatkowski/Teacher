package com.example.teacherapp.ui.screens.other.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.components.pickers.DatePicker
import com.example.teacherapp.ui.components.pickers.TimePicker
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun LessonPlanningScreen() {
    val lessons = remember { mutableStateListOf<Lesson>() }
    var lessonDate by remember { mutableStateOf(LocalDate.now()) }
    var lessonTime by remember { mutableStateOf(LocalTime.of(9, 0)) }

    val dateFormat = remember { DateTimeFormatter.ofPattern("dd LLLL yyyy") }
    val timeFormat = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(MaterialTheme.spacing.large)
    ) {
        Text(
            text = "Lesson Planning",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            LessonDatePicker(
                date = lessonDate,
                onDateSelected = { date ->
                    lessonDate = date
                },
                dateFormat = dateFormat,
            )
            LessonTimePicker(
                lessonTime,
                onTimeSelected = { time ->
                    lessonTime = time
                },
                timeFormat = timeFormat,
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        LessonForm(
            lessonDate = lessonDate,
            lessonTime = lessonTime,
            onLessonAdded = { lesson ->
                lessons.add(lesson)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        LessonList(lessons = lessons, dateFormat = dateFormat, timeFormat = timeFormat)
    }
}

@Composable
private fun LessonForm(
    lessonDate: LocalDate,
    lessonTime: LocalTime,
    onLessonAdded: (Lesson) -> Unit
) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(MaterialTheme.spacing.large)
    ) {
        Text(
            text = "Add Lesson",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Button(
            onClick = {
                val lesson = Lesson(
                    title = title.value,
                    description = description.value,
                    date = lessonDate,
                    time = lessonTime
                )
                onLessonAdded(lesson)

                // Clear form fields
                title.value = ""
                description.value = ""
            }
        ) {
            Text(text = "Add Lesson")
        }
    }
}

@Composable
private fun LessonList(
    lessons: List<Lesson>,
    dateFormat: DateTimeFormatter,
    timeFormat: DateTimeFormatter,
) {
    LazyColumn {
        items(lessons.size) { index ->
            val lesson = lessons[index]
            LessonItem(
                lesson = lesson,
                dateFormat = dateFormat,
                timeFormat = timeFormat,
            )
        }
    }
}

@Composable
private fun LessonItem(
    lesson: Lesson,
    dateFormat: DateTimeFormatter,
    timeFormat: DateTimeFormatter,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.small)
    ) {
        Text(
            text = lesson.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

        Text(text = lesson.description)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

        Text(
            text = "${lesson.date.format(dateFormat)} at ${lesson.time.format(timeFormat)}",
            color = Color.Gray
        )
    }
}

@Composable
private fun LessonDatePicker(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    dateFormat: DateTimeFormatter,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Date:")
        Text(text = date.format(dateFormat))
        DatePicker(
            date = date,
            onDateSelected = onDateSelected,
            label = {
                Card {
                    Text(modifier = Modifier.padding(MaterialTheme.spacing.small), text = "Pick date")
                }
            },
        )
    }
}

@Composable
private fun LessonTimePicker(
    time: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    timeFormat: DateTimeFormatter,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Time:")
        Text(text = time.format(timeFormat))
        TimePicker(time = time, onTimeSelected = onTimeSelected, label = {
            Card {
                Text(modifier = Modifier.padding(MaterialTheme.spacing.small), text = "Pick time")
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonPlanningScreenPreview() {
    TeacherAppTheme {
        LessonPlanningScreen()
    }
}

data class Lesson(
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime
)