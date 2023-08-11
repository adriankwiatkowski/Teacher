package com.example.teacherapp.core.data.repository.event

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.EventType
import com.example.teacherapp.core.model.data.LessonWithSchoolYear
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

interface EventRepository {

    fun getEvents(date: LocalDate): Flow<Result<List<Event>>>

    fun getLessonWithSchoolYearOrNullById(lessonId: Long): Flow<Result<LessonWithSchoolYear?>>

    suspend fun insertEvent(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: EventType,
    )

    suspend fun insertLessonSchedule(
        lessonId: Long,
        day: DayOfWeek,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isFirstTermSelected: Boolean,
        type: EventType,
    )

    suspend fun deleteEventById(id: Long)
}