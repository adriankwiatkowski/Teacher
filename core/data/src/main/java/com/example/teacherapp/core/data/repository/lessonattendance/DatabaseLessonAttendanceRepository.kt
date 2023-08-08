package com.example.teacherapp.core.data.repository.lessonattendance

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.database.datasource.event.EventDataSource
import com.example.teacherapp.core.database.datasource.lessonattendance.LessonAttendanceDataSource
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonEventAttendance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseLessonAttendanceRepository @Inject constructor(
    private val eventDataSource: EventDataSource,
    private val dataSource: LessonAttendanceDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonAttendanceRepository {

    override fun getLessonEventAttendancesByLessonId(
        lessonId: Long
    ): Flow<Result<List<LessonEventAttendance>>> = dataSource
        .getLessonEventAttendancesByLessonId(lessonId)
        .asResult()

    override fun getLessonAttendancesByEventId(
        eventId: Long
    ): Flow<Result<List<LessonAttendance>>> = dataSource
        .getLessonAttendancesByEventId(eventId)
        .asResult()

    override fun getEventById(
        eventId: Long
    ): Flow<Result<Event>> = eventDataSource
        .getEventById(eventId)
        .asResultNotNull()

    override suspend fun insertOrUpdateLessonAttendance(
        eventId: Long,
        studentId: Long,
        attendance: Attendance
    ) {
        scope.launch {
            dataSource.insertOrUpdateLessonAttendance(
                eventId = eventId,
                studentId = studentId,
                attendance = attendance,
            )
        }
    }

    override suspend fun deleteLessonAttendance(eventId: Long, studentId: Long) {
        scope.launch {
            dataSource.deleteLessonAttendance(
                eventId = eventId,
                studentId = studentId,
            )
        }
    }
}