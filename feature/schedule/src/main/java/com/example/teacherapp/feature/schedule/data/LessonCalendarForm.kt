package com.example.teacherapp.feature.schedule.data

import java.time.LocalDate
import java.time.LocalTime

internal data class LessonCalendarForm(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val type: LessonCalendarFormType,
)

internal enum class LessonCalendarFormType {
    Once, Weekly, EveryTwoWeeks
}