package com.example.teacher.core.database.datasource.event

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.datasource.utils.querymapper.toExternal
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.model.EventDto
import com.example.teacher.core.model.data.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
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

    override suspend fun updateEvent(
        id: Long,
        lessonId: Long?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isValid: Boolean
    ) {
        queries.updateEvent(
            id = id,
            lesson_id = lessonId,
            date = date,
            start_time = startTime,
            end_time = endTime,
            is_valid = isValid,
        )
    }

    override suspend fun deleteEventById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteEventById(id)
    }
}