package com.example.teacherapp.core.data.repository.lessonattendance

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.database.datasource.lessonattendance.LessonAttendanceDataSource
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonScheduleAttendance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseLessonAttendanceRepository @Inject constructor(
    private val dataSource: LessonAttendanceDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonAttendanceRepository {

    override fun getLessonScheduleAttendancesByLessonId(
        lessonId: Long
    ): Flow<Result<List<LessonScheduleAttendance>>> = dataSource
        .getLessonScheduleAttendancesByLessonId(lessonId)
        .asResult()

    override fun getLessonAttendancesByLessonId(
        lessonId: Long
    ): Flow<Result<List<LessonAttendance>>> = dataSource
        .getLessonAttendancesByLessonId(lessonId)
        .asResult()

    override suspend fun insertOrUpdateLessonAttendance(
        lessonScheduleId: Long,
        studentId: Long,
        attendance: Attendance
    ) {
        scope.launch {
            dataSource.insertOrUpdateLessonAttendance(
                lessonScheduleId = lessonScheduleId,
                studentId = studentId,
                attendance = attendance,
            )
        }
    }

    override suspend fun deleteLessonAttendance(lessonScheduleId: Long, studentId: Long) {
        scope.launch {
            dataSource.deleteLessonAttendance(
                lessonScheduleId = lessonScheduleId,
                studentId = studentId,
            )
        }
    }
}