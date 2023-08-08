package com.example.teacherapp.core.database.datasource.event

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.database.model.EventDto
import com.example.teacherapp.core.model.data.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

internal class EventDataSourceImpl @Inject constructor(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : EventDataSource {

    private val queries = db.eventQueries

    override fun getEvents(date: LocalDate): Flow<List<Event>> =
        queries
            .getEvents(date)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getEventById(eventId: Long): Flow<Event?> =
        queries
            .getEventById(eventId)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertEvents(
        eventDtos: List<EventDto>,
    ): Unit = withContext(dispatcher) {
        queries.transaction {
            for (eventDto in eventDtos) {
                queries.insertEvent(
                    id = eventDto.id,
                    lesson_id = eventDto.lessonId,
                    date = eventDto.date,
                    start_time = eventDto.startTime,
                    end_time = eventDto.endTime,
                    is_valid = true,
                )
            }
        }
    }

    override suspend fun deleteEventById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteEventById(id)
    }
}