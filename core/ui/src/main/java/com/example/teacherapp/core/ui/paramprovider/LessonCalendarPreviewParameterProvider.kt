package com.example.teacherapp.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.LessonSchedule
import java.time.LocalDate
import java.time.LocalTime

class LessonSchedulesPreviewParameterProvider : PreviewParameterProvider<List<LessonSchedule>> {
    override val values: Sequence<List<LessonSchedule>> = sequenceOf(lessonSchedules)
}

class LessonSchedulePreviewParameterProvider : PreviewParameterProvider<LessonSchedule> {
    override val values: Sequence<LessonSchedule> = lessonSchedules.asSequence()
}

private val lessonSchedules = makeLessonSchedules(
    SimpleLessonSchedule(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 0),
        endTime = TimeUtils.localTimeOf(8, 45),
    ),
    SimpleLessonSchedule(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 50),
        endTime = TimeUtils.localTimeOf(9, 35),
    ),
    SimpleLessonSchedule(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(9, 50),
        endTime = TimeUtils.localTimeOf(10, 35),
    ),
    SimpleLessonSchedule(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(10, 40),
        endTime = TimeUtils.localTimeOf(11, 25),
    ),
)

private fun makeLessonSchedules(
    vararg lesson: SimpleLessonSchedule,
): List<LessonSchedule> {
    return lesson.mapIndexed { index, simpleLessonCalendar ->
        LessonSchedule(
            id = index.toLong(),
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
        )
    }
}

private data class SimpleLessonSchedule(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)