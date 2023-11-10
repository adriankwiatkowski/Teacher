package com.example.teacher.core.data.repository.lessonattendance

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.LessonAttendance
import com.example.teacher.core.model.data.LessonEventAttendance
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.StudentWithAttendance
import kotlinx.coroutines.flow.Flow

interface LessonAttendanceRepository {

    fun getLessonEventAttendancesByLessonId(
        lessonId: Long
    ): Flow<Result<List<LessonEventAttendance>>>

    fun getLessonAttendancesByEventId(eventId: Long): Flow<Result<List<LessonAttendance>>>

    fun getEventById(eventId: Long): Flow<Result<Event>>

    fun getStudentsWithAttendanceByLessonId(
        lessonId: Long
    ): Flow<Result<List<StudentWithAttendance>>>

    fun getSchoolYearByLessonId(lessonId: Long): Flow<Result<SchoolYear>>

    suspend fun insertOrUpdateLessonAttendance(
        eventId: Long,
        studentId: Long,
        attendance: Attendance,
    )

    suspend fun deleteLessonAttendance(eventId: Long, studentId: Long)
}