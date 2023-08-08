package com.example.teacherapp.core.data.repository.lessonattendance

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonEventAttendance
import kotlinx.coroutines.flow.Flow

interface LessonAttendanceRepository {

    fun getLessonEventAttendancesByLessonId(
        lessonId: Long
    ): Flow<Result<List<LessonEventAttendance>>>

    fun getLessonAttendancesByEventId(eventId: Long): Flow<Result<List<LessonAttendance>>>

    fun getEventById(eventId: Long): Flow<Result<Event>>

    suspend fun insertOrUpdateLessonAttendance(
        eventId: Long,
        studentId: Long,
        attendance: Attendance,
    )

    suspend fun deleteLessonAttendance(eventId: Long, studentId: Long)
}