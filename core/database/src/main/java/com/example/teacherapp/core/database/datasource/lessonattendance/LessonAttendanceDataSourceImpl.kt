package com.example.teacherapp.core.database.datasource.lessonattendance

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternalLessonScheduleAttendances
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonScheduleAttendance
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

    override fun getLessonScheduleAttendancesByLessonId(
        lessonId: Long
    ): Flow<List<LessonScheduleAttendance>> =
        queries
            .getLessonSchedulesByLessonId(lessonId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternalLessonScheduleAttendances)
            .flowOn(dispatcher)

    override fun getLessonAttendancesByLessonId(lessonId: Long): Flow<List<LessonAttendance>> =
        queries
            .getLessonAttendancesByLessonId(lessonId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateLessonAttendance(
        lessonScheduleId: Long,
        studentId: Long,
        attendance: Attendance,
    ): Unit = withContext(dispatcher) {
        val attendanceId =
            queries.getAttendanceByText(attendance.text).executeAsOne().id

        queries.insertOrUpdateLessonAttendance(
            lesson_schedule_id = lessonScheduleId,
            student_id = studentId,
            attendance_id = attendanceId,
        )
    }

    override suspend fun deleteLessonAttendance(
        lessonScheduleId: Long,
        studentId: Long,
    ): Unit = withContext(dispatcher) {
        queries.deleteLessonAttendance(
            lesson_schedule_id = lessonScheduleId,
            student_id = studentId,
        )
    }
}