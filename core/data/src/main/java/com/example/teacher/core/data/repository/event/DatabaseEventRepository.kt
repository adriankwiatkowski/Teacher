package com.example.teacher.core.data.repository.event

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.datasource.event.EventDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.model.EventDto
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.Term
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

internal class DatabaseEventRepository @Inject constructor(
    private val eventDataSource: EventDataSource,
    private val schoolYearDataSource: SchoolYearDataSource,
    private val lessonDataSource: LessonDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : EventRepository {

    override fun getEvents(date: LocalDate): Flow<Result<List<Event>>> = eventDataSource
        .getEvents(date)
        .asResult()

    override fun getEventOrNullById(id: Long): Flow<Result<Event?>> = eventDataSource
        .getEventById(id)
        .asResult()

    override fun getLessonOrNullById(lessonId: Long): Flow<Result<Lesson?>> = lessonDataSource
        .getLessonById(lessonId)
        .asResult()

    override suspend fun insertEvent(
        name: String,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isCancelled: Boolean,
        type: EventType,
    ) {
        scope.launch {
            eventDataSource.insertEvents(
                listOf(
                    EventDto(
                        id = null,
                        lessonId = null,
                        name = name,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        isCancelled = isCancelled,
                    ),
                )
            )
        }
    }

    override suspend fun insertLessonSchedule(
        lessonId: Long,
        name: String,
        day: DayOfWeek,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isCancelled: Boolean,
        isFirstTermSelected: Boolean,
        type: EventType
    ) {
        scope.launch {
            withContext(dispatcher) {
                val schoolYear = schoolYearDataSource.getSchoolYearByLessonId(lessonId)!!
                val term = with(schoolYear) { if (isFirstTermSelected) firstTerm else secondTerm }

                val eventDtos = mutableListOf<EventDto>()

                fun addDate(date: LocalDate) {
                    eventDtos.add(
                        EventDto(
                            id = null,
                            lessonId = lessonId,
                            name = name,
                            date = date,
                            startTime = startTime,
                            endTime = endTime,
                            isCancelled = isCancelled,
                        )
                    )
                }

                fun addDatesInTerm(daysOffset: Long) {
                    var currentDateOfSelectedDay =
                        TimeUtils.firstDayOfWeekFromDate(term.startDate, day)

                    while (true) {
                        if (!isDateInTerm(currentDateOfSelectedDay, term)) {
                            break
                        }

                        addDate(currentDateOfSelectedDay)
                        currentDateOfSelectedDay =
                            TimeUtils.plusDays(currentDateOfSelectedDay, daysOffset)
                    }
                }

                when (type) {
                    EventType.Once -> addDate(date)
                    EventType.Weekly -> addDatesInTerm(daysOffset = 7)
                    EventType.EveryTwoWeeks -> addDatesInTerm(daysOffset = 14)
                }

                eventDataSource.insertEvents(eventDtos)
            }
        }
    }

    override suspend fun updateEvent(
        id: Long,
        lessonId: Long?,
        name: String,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        isCancelled: Boolean,
    ) {
        scope.launch {
            eventDataSource.updateEvent(
                id = id,
                lessonId = lessonId,
                name = name,
                date = date,
                startTime = startTime,
                endTime = endTime,
                isCancelled = isCancelled,
            )
        }
    }

    override suspend fun deleteEventById(id: Long) {
        scope.launch {
            eventDataSource.deleteEventById(id)
        }
    }

    private fun isDateInTerm(date: LocalDate, term: Term): Boolean {
        return TimeUtils.isBetween(date, term.startDate, term.endDate)
    }
}