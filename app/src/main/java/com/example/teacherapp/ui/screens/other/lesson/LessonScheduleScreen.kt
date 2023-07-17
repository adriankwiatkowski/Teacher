package com.example.teacherapp.ui.screens.other.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun LessonScheduleScreen() {
    val daysOfWeek = remember { listOf("Mon", "Tue", "Wed", "Thu", "Fri") }
    val startTime = 8
    val endTime = 20
    val lessons = remember {
        listOf(
            LessonSchedule("Lesson 1", 8, 9, "Mon"),
            LessonSchedule("Lesson 1", 10, 12, "Mon"),
            LessonSchedule("Lesson 2", 13, 14, "Mon"),
            LessonSchedule("Lesson 2", 19, 20, "Mon"),
            LessonSchedule("Lesson 3", 8, 10, "Tue"),
            LessonSchedule("Lesson 4", 8, 10, "Wed"),
        )
    }

    Column(
        Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small)
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(
                        top = MaterialTheme.spacing.extraLarge,
                        end = MaterialTheme.spacing.small,
                    )
            ) {
                for (time in startTime..endTime) {
                    Hour(modifier = Modifier.weight(1f), time = "$time:00")
                }
            }

            for (day in daysOfWeek) {
                Box(Modifier.weight(1f)) {
                    Column(Modifier.fillMaxSize()) {
                        BoxWithConstraints(Modifier.fillMaxWidth()) {
                            Header(
                                modifier = Modifier.fillMaxWidth(),
                                day = day,
                                isFirst = day == daysOfWeek.first(),
                                isLast = day == daysOfWeek.last(),
                            )
                        }

                        Lessons(
                            modifier = Modifier.weight(1f),
                            day = day,
                            startTime = startTime,
                            endTime = endTime,
                            lessons = lessons,
                        )
                    }

                    Column(
                        Modifier
                            .padding(top = MaterialTheme.spacing.extraLarge)
                            .fillMaxSize()
                    ) {
                        repeat(endTime - startTime + 1) {
                            Box(Modifier.weight(1f), contentAlignment = Alignment.TopStart) {
                                Divider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color.Red,
                                    thickness = 1.dp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class LessonSchedule(val name: String, val startTime: Int, val endTime: Int, val day: String)

@Composable
private fun Lessons(
    day: String,
    startTime: Int,
    endTime: Int,
    lessons: List<LessonSchedule>,
    modifier: Modifier = Modifier,
) {
    var currentTime = startTime

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        for (lesson in lessons) {
            if (day == lesson.day) {
                run {
                    val diff = lesson.startTime - currentTime
                    if (diff > 0) {
                        Box(Modifier.weight(diff.toFloat()))
                        currentTime += diff
                    }
                }

                run {
                    val diff = lesson.endTime - lesson.startTime
                    val backgroundColor = MaterialTheme.colorScheme.primary
                    val shape = MaterialTheme.shapes.medium
                    Box(
                        modifier = Modifier
                            .weight(diff.toFloat())
                            .background(color = backgroundColor, shape = shape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = lesson.name,
                            textAlign = TextAlign.Center,
                            color = contentColorFor(backgroundColor = backgroundColor),
                        )
                    }
                    currentTime += diff
                }
            }
        }

        if (currentTime < endTime - 2) {
            val diff = endTime - currentTime
            Box(Modifier.weight(diff.toFloat()))
        }

        Box(Modifier.weight(1f))
    }
}

@Composable
private fun Header(
    day: String,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = day,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .border(
                1.dp,
                Color.Gray,
                RoundedCornerShape(
                    topStart = if (isFirst) MaterialTheme.spacing.small else 0.dp,
                    topEnd = if (isLast) MaterialTheme.spacing.small else 0.dp
                )
            )
            .padding(4.dp)
    )
}

@Composable
private fun Hour(
    time: String,
    modifier: Modifier = Modifier,
) {
    Text(modifier = modifier, text = time, textAlign = TextAlign.End)
}

@Composable
private fun LessonScheduleScreen2() {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    val startTime = 8
    val endTime = 20

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Lesson Schedule",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Box(Modifier.weight(1f)) {
            Column(Modifier.fillMaxSize()) {
                // Header row
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                Color.Gray,
                                RoundedCornerShape(topStart = MaterialTheme.spacing.small)
                            )
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Time",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    for (day in daysOfWeek) {
                        Box(
                            Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    Color.Gray,
                                    RoundedCornerShape(
                                        topEnd = if (day == daysOfWeek.last()) MaterialTheme.spacing.small else 0.dp,
                                        topStart = if (day == daysOfWeek.first()) MaterialTheme.spacing.small else 0.dp
                                    )
                                )
                                .padding(4.dp)
                        ) {
                            Text(
                                text = day,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                // Lesson rows
                for (time in startTime..endTime) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Box(
                            Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    Color.Gray,
                                    RoundedCornerShape(
                                        bottomStart = if (time == startTime) MaterialTheme.spacing.small else 0.dp
                                    )
                                )
                                .padding(4.dp)
                        ) {
                            Text(
                                text = time.toString(),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }
                        for (day in daysOfWeek) {
//                            val lesson = lessons[day]?.find { it.startTime == time }
                            val lesson = "Lesson 23423 234423524 25425"
                            Box(
                                Modifier
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        Color.Gray,
                                        RoundedCornerShape(
                                            bottomEnd = if (time == endTime) MaterialTheme.spacing.small else 0.dp
                                        )
                                    )
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = lesson,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LessonScheduleScreenPreview() {
    TeacherAppTheme {
        LessonScheduleScreen()
    }
}
