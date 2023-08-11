package com.example.teacherapp.core.data.repository.event

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.database.datasource.event.EventDataSource
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSource
import com.example.teacherapp.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacherapp.core.database.model.EventDto
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.EventType
import com.example.teacherapp.core.model.data.LessonWithSchoolYear
import com.example.teacherapp.core.model.data.Term
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

    override fun getLessonWithSchoolYearOrNullById(
        lessonId: Long
    ): Flow<Result<LessonWithSchoolYear?>> = lessonDataSource
        .getLessonWithSchoolYearById(lessonId)
        .asResult()

    override suspend fun insertEvent(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: EventType,
    ) {
        scope.launch {
            eventDataSource.insertEvents(
                listOf(
                    EventDto(
                        id = null,
                        lessonId = null,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                    ),
                )
            )
        }
    }

    override suspend fun insertLessonSchedule(
        lessonId: Long,
        day: DayOfWeek,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
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
                            date = date,
                            startTime = startTime,
                            endTime = endTime,
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
                    EventType.Once -> addDate(date) // TODO: Probably should check if date is in any term.
                    EventType.Weekly -> addDatesInTerm(daysOffset = 7)
                    EventType.EveryTwoWeeks -> addDatesInTerm(daysOffset = 14)
                }

                eventDataSource.insertEvents(eventDtos)
            }
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