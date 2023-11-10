package com.example.teacher.feature.lesson.attendance.data

import com.example.teacher.core.model.data.LessonEventAttendance

internal data class AttendancesUiState(
    val firstTermScheduleAttendances: List<LessonEventAttendance>,
    val secondTermScheduleAttendances: List<LessonEventAttendance>,
    val scheduleAttendancesWithoutTerm: List<LessonEventAttendance>,
) {
    fun isEmpty() = firstTermScheduleAttendances.isEmpty()
            && secondTermScheduleAttendances.isEmpty()
            && scheduleAttendancesWithoutTerm.isEmpty()
}