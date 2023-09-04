package com.example.teacher.core.model.data

data class LessonAttendance(
    val eventId: Long,
    val student: BasicStudent,
    val attendance: Attendance?,
)