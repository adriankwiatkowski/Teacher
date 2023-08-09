package com.example.teacherapp.core.data.repository.event

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.EventType
import com.example.teacherapp.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

interface EventRepository {

    fun getEvents(date: LocalDate): Flow<Result<List<Event>>>

    fun getLessonOrNullById(lessonId: Long): Flow<Result<Lesson?>>

    suspend fun insertEvent(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: EventType,
    )

    suspend fun insertLessonSchedule(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: EventType,
    )

    suspend fun deleteEventById(id: Long)
}