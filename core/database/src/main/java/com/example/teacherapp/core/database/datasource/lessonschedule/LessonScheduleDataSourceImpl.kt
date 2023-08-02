package com.example.teacherapp.core.database.datasource.lessonschedule

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.database.model.LessonScheduleDto
import com.example.teacherapp.core.model.data.LessonSchedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
        lessonScheduleDtos: List<LessonScheduleDto>,
    ): Unit = withContext(dispatcher) {
        queries.transaction {
            for (lessonScheduleDto in lessonScheduleDtos) {
                queries.insertLessonSchedule(
                    id = lessonScheduleDto.id,
                    lesson_id = lessonScheduleDto.lessonId,
                    date = lessonScheduleDto.date,
                    start_time = lessonScheduleDto.startTime,
                    end_time = lessonScheduleDto.endTime,
                    is_valid = true,
                )
            }
        }
    }

    override suspend fun deleteLessonScheduleById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteLessonScheduleById(id)
    }
}