package com.example.teacherapp.core.model.data

import java.time.LocalDate
import java.time.LocalTime

data class Event(
    val id: Long,
    val lesson: Lesson,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isValid: Boolean,
)
