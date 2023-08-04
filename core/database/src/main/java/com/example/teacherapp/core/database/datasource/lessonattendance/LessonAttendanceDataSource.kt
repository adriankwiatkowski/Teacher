package com.example.teacherapp.core.database.datasource.lessonattendance

import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonScheduleAttendance
import kotlinx.coroutines.flow.Flow

interface LessonAttendanceDataSource {

    fun getLessonScheduleAttendancesByLessonId(
        lessonId: Long
    ): Flow<List<LessonScheduleAttendance>>

    fun getLessonAttendancesByLessonId(lessonId: Long): Flow<List<LessonAttendance>>

    suspend fun insertOrUpdateLessonAttendance(
        lessonScheduleId: Long,
        studentId: Long,
        attendance: Attendance,
    )

    suspend fun deleteLessonAttendance(lessonScheduleId: Long, studentId: Long)
}