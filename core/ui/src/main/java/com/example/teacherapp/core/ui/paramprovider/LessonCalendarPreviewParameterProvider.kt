package com.example.teacherapp.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.LessonCalendar
import java.time.LocalDate
import java.time.LocalTime

class LessonCalendarsPreviewParameterProvider : PreviewParameterProvider<List<LessonCalendar>> {
    override val values: Sequence<List<LessonCalendar>> = sequenceOf(lessonCalendars)
}

class LessonCalendarPreviewParameterProvider : PreviewParameterProvider<LessonCalendar> {
    override val values: Sequence<LessonCalendar> = lessonCalendars.asSequence()
}

private val lessonCalendars = makeLessonCalendars(
    SimpleLessonCalendar(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 0),
        endTime = TimeUtils.localTimeOf(8, 45),
    ),
    SimpleLessonCalendar(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 50),
        endTime = TimeUtils.localTimeOf(9, 35),
    ),
    SimpleLessonCalendar(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(9, 50),
        endTime = TimeUtils.localTimeOf(10, 35),
    ),
    SimpleLessonCalendar(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(10, 40),
        endTime = TimeUtils.localTimeOf(11, 25),
    ),
)

private fun makeLessonCalendars(
    vararg lesson: SimpleLessonCalendar,
): List<LessonCalendar> {
    return lesson.mapIndexed { index, simpleLessonCalendar ->
        LessonCalendar(
            id = index.toLong(),
            lessonId = 1L,
            date = simpleLessonCalendar.date,
            startTime = simpleLessonCalendar.startTime,
            endTime = simpleLessonCalendar.endTime,
            isValid = true,
        )
    }
}

private data class SimpleLessonCalendar(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)