package com.example.teacherapp.core.model.data

data class LessonAttendance(
    val lessonScheduleId: Long,
    val student: BasicStudent,
    val attendance: Attendance,
)