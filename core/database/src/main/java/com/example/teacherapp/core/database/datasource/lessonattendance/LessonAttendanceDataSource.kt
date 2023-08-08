package com.example.teacherapp.core.database.datasource.lessonattendance

import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonEventAttendance
import kotlinx.coroutines.flow.Flow

interface LessonAttendanceDataSource {

    fun getLessonEventAttendancesByLessonId(lessonId: Long): Flow<List<LessonEventAttendance>>

    fun getLessonAttendancesByEventId(eventId: Long): Flow<List<LessonAttendance>>

    suspend fun insertOrUpdateLessonAttendance(
        eventId: Long,
        studentId: Long,
        attendance: Attendance,
    )

    suspend fun deleteLessonAttendance(eventId: Long, studentId: Long)
}