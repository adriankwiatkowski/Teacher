package com.example.teacher.feature.lesson.attendance.data

import com.example.teacher.core.model.data.Attendance

internal data class DialogState(
    val studentId: Long,
    val studentFullName: String,
    val attendance: Attendance?,
)