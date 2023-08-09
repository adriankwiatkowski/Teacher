package com.example.teacherapp.core.database.datasource.lessonattendance

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternalLessonEventAttendances
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonEventAttendance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LessonAttendanceDataSourceImpl @Inject constructor(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : LessonAttendanceDataSource {

    private val queries = db.lessonAttendanceQueries

    override fun getLessonEventAttendancesByLessonId(
        lessonId: Long
    ): Flow<List<LessonEventAttendance>> =
        queries
            .getLessonEventsByLessonId(lessonId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternalLessonEventAttendances)
            .flowOn(dispatcher)

    override fun getLessonAttendancesByEventId(eventId: Long): Flow<List<LessonAttendance>> =
        queries
            .getLessonAttendancesByEventId(eventId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateLessonAttendance(
        eventId: Long,
        studentId: Long,
        attendance: Attendance,
    ): Unit = withContext(dispatcher) {
        val attendanceId =
            queries.getAttendanceByText(attendance.text).executeAsOne().id

        queries.insertOrUpdateLessonAttendance(
            event_id = eventId,
            student_id = studentId,
            attendance_id = attendanceId,
        )
    }

    override suspend fun deleteLessonAttendance(
        eventId: Long,
        studentId: Long,
    ): Unit = withContext(dispatcher) {
        queries.deleteLessonAttendance(
            event_id = eventId,
            student_id = studentId,
        )
    }
}