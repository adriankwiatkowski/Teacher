package com.example.teacherapp.core.database.model

import java.time.LocalDate
import java.time.LocalTime

data class LessonScheduleDto(
    val id: Long?,
    val lessonId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)
