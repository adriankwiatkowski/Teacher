package com.example.teacher.core.data.repository.event

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

interface EventRepository {

    fun getEvents(date: LocalDate): Flow<Result<List<Event>>>

    fun getEventOrNullById(id: Long): Flow<Result<Event?>>

    fun getLessonOrNullById(lessonId: Long): Flow<Result<Lesson?>>

    suspend fun insertEvent(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isCancelled: Boolean,
        type: EventType,
    )

    suspend fun insertLessonSchedule(
        lessonId: Long,
        day: DayOfWeek,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isCancelled: Boolean,
        isFirstTermSelected: Boolean,
        type: EventType,
    )

    suspend fun updateEvent(
        id: Long,
        lessonId: Long?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isCancelled: Boolean,
    )

    suspend fun deleteEventById(id: Long)
}