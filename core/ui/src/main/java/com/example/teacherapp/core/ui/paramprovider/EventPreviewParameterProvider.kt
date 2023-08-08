package com.example.teacherapp.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.Lesson
import java.time.LocalDate
import java.time.LocalTime

class EventsPreviewParameterProvider : PreviewParameterProvider<List<Event>> {
    override val values: Sequence<List<Event>> = sequenceOf(events)
}

private val events = makeEvents(
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 0),
        endTime = TimeUtils.localTimeOf(8, 45),
    ),
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 50),
        endTime = TimeUtils.localTimeOf(9, 35),
    ),
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(9, 50),
        endTime = TimeUtils.localTimeOf(10, 35),
    ),
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(10, 40),
        endTime = TimeUtils.localTimeOf(11, 25),
    ),
)

private fun makeEvents(
    vararg lesson: SimpleEvent,
): List<Event> {
    return lesson.mapIndexed { index, simpleLessonCalendar ->
        listOf(
            Event(
                id = index.toLong() * 2,
                lesson = Lesson(
                    id = 1L,
                    name = "Matematyka",
                    schoolClass = BasicSchoolClass(
                        id = 1L,
                        name = "1A",
                        studentCount = 20,
                        lessonCount = 4,
                    )
                ),
                date = simpleLessonCalendar.date,
                startTime = simpleLessonCalendar.startTime,
                endTime = simpleLessonCalendar.endTime,
                isValid = true,
            ),
            Event(
                id = index.toLong() * 2 + 1,
                lesson = null,
                date = simpleLessonCalendar.date,
                startTime = simpleLessonCalendar.startTime,
                endTime = simpleLessonCalendar.endTime,
                isValid = true,
            )
        )
    }.flatten()
}

private data class SimpleEvent(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)