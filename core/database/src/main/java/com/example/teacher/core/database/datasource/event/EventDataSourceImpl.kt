package com.example.teacher.core.database.datasource.event

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.model.EventDto
import com.example.teacher.core.database.querymapper.toExternal
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

    private val eventQueries = db.eventQueries

    override fun getEvents(date: LocalDate): Flow<List<Event>> =
        eventQueries
            .getEvents(date)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getEventById(eventId: Long): Flow<Event?> =
        eventQueries
            .getEventById(eventId)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertEvents(
        eventDtos: List<EventDto>,
    ): Unit = withContext(dispatcher) {
        eventQueries.transaction {
            for (eventDto in eventDtos) {
                eventQueries.insertEvent(
                    id = eventDto.id,
                    lesson_id = eventDto.lessonId,
                    date = eventDto.date,
                    start_time = eventDto.startTime,
                    end_time = eventDto.endTime,
                    is_cancelled = eventDto.isCancelled,
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
        isCancelled: Boolean
    ): Unit = withContext(dispatcher) {
        eventQueries.transaction {
            removeOldLessonAttendances(eventId = id, newLessonId = lessonId)

            eventQueries.updateEvent(
                id = id,
                lesson_id = lessonId,
                date = date,
                start_time = startTime,
                end_time = endTime,
                is_cancelled = isCancelled,
            )
        }
    }

    override suspend fun deleteEventById(id: Long): Unit = withContext(dispatcher) {
        eventQueries.deleteAttendancesByEventId(id)
        eventQueries.deleteEventById(id)
    }

    private fun removeOldLessonAttendances(eventId: Long, newLessonId: Long?) {
        if (didSchoolClassChanged(eventId = eventId, newLessonId = newLessonId)) {
            eventQueries.deleteAttendancesByEventId(eventId)
        }
    }

    private fun didSchoolClassChanged(eventId: Long, newLessonId: Long?): Boolean {
        val oldEvent = eventQueries.getEventById(eventId).executeAsOne()

        if (newLessonId != oldEvent.lesson_id) {
            // Previously had school class, and now don't.
            if (newLessonId == null) {
                return true
            }

            val oldSchoolClassId = oldEvent.school_class_id
            val newSchoolClassId =
                eventQueries.getSchoolClassIdByLessonId(newLessonId).executeAsOne()

            // School id doesn't match.
            if (oldSchoolClassId != newSchoolClassId) {
                return true
            }
        }

        return false
    }
}