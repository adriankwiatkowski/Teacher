package com.example.teacherapp.core.data.repository.lessonattendance

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonSchedule
import com.example.teacherapp.core.model.data.LessonScheduleAttendance
import kotlinx.coroutines.flow.Flow

interface LessonAttendanceRepository {

    fun getLessonScheduleAttendancesByLessonId(
        lessonId: Long
    ): Flow<Result<List<LessonScheduleAttendance>>>

    fun getLessonAttendancesByLessonScheduleId(
        lessonScheduleId: Long
    ): Flow<Result<List<LessonAttendance>>>

    fun getLessonScheduleById(lessonScheduleId: Long): Flow<Result<LessonSchedule>>

    suspend fun insertOrUpdateLessonAttendance(
        lessonScheduleId: Long,
        studentId: Long,
        attendance: Attendance,
    )

    suspend fun deleteLessonAttendance(lessonScheduleId: Long, studentId: Long)
}