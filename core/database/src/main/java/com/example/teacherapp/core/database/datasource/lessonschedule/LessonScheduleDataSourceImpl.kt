package com.example.teacherapp.core.database.datasource.lessonschedule

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.LessonSchedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

internal class LessonScheduleDataSourceImpl @Inject constructor(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : LessonScheduleDataSource {

    private val queries = db.lessonScheduleQueries

    override fun getLessonSchedules(): Flow<List<LessonSchedule>> =
        queries
            .getLessonSchedules()
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertLessonSchedule(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): Unit = withContext(dispatcher) {
        queries.insertLessonSchedule(
            id = null,
            lesson_id = lessonId,
            date = date,
            start_time = startTime,
            end_time = endTime,
            is_valid = true,
        )
    }

    override suspend fun deleteLessonScheduleById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteLessonScheduleById(id)
    }
}