package com.example.teacherapp.core.data.repository.event

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.database.datasource.event.EventDataSource
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSource
import com.example.teacherapp.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacherapp.core.database.model.EventDto
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.EventType
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.Term
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    override fun getLessonById(lessonId: Long): Flow<Result<Lesson>> = lessonDataSource
        .getLessonById(lessonId)
        .asResultNotNull()

    override suspend fun insertEvent(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: EventType,
    ) {
        scope.launch {
            withContext(dispatcher) {
                val schoolYear = schoolYearDataSource.getSchoolYearByLessonId(lessonId)!!
                val term: Term? = when {
                    isDateInTerm(date, schoolYear.firstTerm) -> schoolYear.firstTerm
                    isDateInTerm(date, schoolYear.secondTerm) -> schoolYear.secondTerm
                    else -> null
                }

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

                fun addDatesUntilEndOfTerm(daysOffset: Long) {
                    if (term == null) {
                        return
                    }

                    var currentDate = date
                    while (true) {
                        val newDate = TimeUtils.plusDays(currentDate, daysOffset)

                        if (!isDateInTerm(newDate, term)) {
                            break
                        }

                        addDate(newDate)
                        currentDate = newDate
                    }
                }

                // Add current date.
                addDate(date)

                // Date isn't in any term, so add date only once ignoring schedule type.
                if (term != null) {
                    when (type) {
                        EventType.Once -> {} // We already added date, so do nothing.
                        EventType.Weekly -> addDatesUntilEndOfTerm(daysOffset = 7)
                        EventType.EveryTwoWeeks -> addDatesUntilEndOfTerm(daysOffset = 14)
                    }
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