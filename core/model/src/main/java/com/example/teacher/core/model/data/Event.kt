package com.example.teacher.core.model.data

import java.time.LocalDate
import java.time.LocalTime

data class Event(
    val id: Long,
    val lesson: Lesson?,
    val name: String?,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isCancelled: Boolean,
)
