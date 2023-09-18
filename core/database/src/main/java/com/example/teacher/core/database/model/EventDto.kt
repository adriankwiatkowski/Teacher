package com.example.teacher.core.database.model

import java.time.LocalDate
import java.time.LocalTime

data class EventDto(
    val id: Long?,
    val lessonId: Long?,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isCancelled: Boolean,
)
