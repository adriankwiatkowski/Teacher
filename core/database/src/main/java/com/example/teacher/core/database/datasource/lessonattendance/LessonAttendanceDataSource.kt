package com.example.teacher.core.database.datasource.lessonattendance

import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.LessonAttendance
import com.example.teacher.core.model.data.LessonEventAttendance
import com.example.teacher.core.model.data.StudentWithAttendance
import kotlinx.coroutines.flow.Flow

interface LessonAttendanceDataSource {

    fun getLessonEventAttendancesByLessonId(lessonId: Long): Flow<List<LessonEventAttendance>>

    fun getLessonAttendancesByEventId(eventId: Long): Flow<List<LessonAttendance>>

    fun getStudentsWithAttendanceByLessonId(lessonId: Long): Flow<List<StudentWithAttendance>>

    suspend fun insertOrUpdateLessonAttendance(
        eventId: Long,
        studentId: Long,
        attendance: Attendance,
    )

    suspend fun deleteLessonAttendance(eventId: Long, studentId: Long)
}