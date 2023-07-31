package com.example.teacherapp.core.model.data

import java.time.LocalDate
import java.time.LocalTime

data class LessonCalendar(
    val id: Long,
    val lessonId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isValid: Boolean,
)
