package com.example.teacherapp.core.database.datasource.event

import com.example.teacherapp.core.database.model.EventDto
import com.example.teacherapp.core.model.data.Event
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EventDataSource {

    fun getEvents(date: LocalDate): Flow<List<Event>>

    fun getEventById(eventId: Long): Flow<Event?>

    suspend fun insertEvents(eventDtos: List<EventDto>)

    suspend fun deleteEventById(id: Long)
}