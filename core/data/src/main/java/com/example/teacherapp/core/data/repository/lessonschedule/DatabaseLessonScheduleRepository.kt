package com.example.teacherapp.core.data.repository.lessonschedule

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSource
import com.example.teacherapp.core.database.datasource.lessonschedule.LessonScheduleDataSource
import com.example.teacherapp.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacherapp.core.database.model.LessonScheduleDto
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.LessonSchedule
import com.example.teacherapp.core.model.data.LessonScheduleType
import com.example.teacherapp.core.model.data.Term
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

internal class DatabaseLessonScheduleRepository @Inject constructor(
    private val lessonScheduleDataSource: LessonScheduleDataSource,
    private val schoolYearDataSource: SchoolYearDataSource,
    private val lessonDataSource: LessonDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonScheduleRepository {

    override fun getLessonSchedules(
        date: LocalDate
    ): Flow<Result<List<LessonSchedule>>> = lessonScheduleDataSource
        .getLessonSchedules(date)
        .asResult()

    override fun getLessonById(lessonId: Long): Flow<Result<Lesson>> = lessonDataSource
        .getLessonById(lessonId)
        .asResultNotNull()

    override suspend fun insertLessonSchedule(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: LessonScheduleType,
    ) {
        scope.launch {
            withContext(dispatcher) {
                val schoolYear = schoolYearDataSource.getSchoolYearByLessonId(lessonId)!!
                val term: Term? = when {
                    isDateInTerm(date, schoolYear.firstTerm) -> schoolYear.firstTerm
                    isDateInTerm(date, schoolYear.secondTerm) -> schoolYear.secondTerm
                    else -> null
                }

                val lessonScheduleDtos = mutableListOf<LessonScheduleDto>()

                fun addDate(date: LocalDate) {
                    lessonScheduleDtos.add(
                        LessonScheduleDto(
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
                        LessonScheduleType.Once -> {} // We already added date, so do nothing.
                        LessonScheduleType.Weekly -> addDatesUntilEndOfTerm(daysOffset = 7)
                        LessonScheduleType.EveryTwoWeeks -> addDatesUntilEndOfTerm(daysOffset = 14)
                    }
                }

                lessonScheduleDataSource.insertLessonSchedule(lessonScheduleDtos)
            }
        }
    }

    override suspend fun deleteLessonScheduleById(id: Long) {
        scope.launch {
            lessonScheduleDataSource.deleteLessonScheduleById(id)
        }
    }

    private fun isDateInTerm(date: LocalDate, term: Term): Boolean {
        return TimeUtils.isBetween(date, term.startDate, term.endDate)
    }
}