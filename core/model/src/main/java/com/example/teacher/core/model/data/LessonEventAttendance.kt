package com.example.teacher.core.model.data

import java.time.LocalDate
import java.time.LocalTime

data class LessonEventAttendance(
    val eventId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isValid: Boolean,
    val presentCount: Long,
    val lateCount: Long,
    val absentCount: Long,
    val excusedAbsenceCount: Long,
    val exemptionCount: Long,
    val attendanceNotSetCount: Long,
)