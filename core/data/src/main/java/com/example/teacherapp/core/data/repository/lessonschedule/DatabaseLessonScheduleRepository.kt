package com.example.teacherapp.core.data.repository.lessonschedule

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSource
import com.example.teacherapp.core.database.datasource.lessonschedule.LessonScheduleDataSource
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.LessonScheduleType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class DatabaseLessonScheduleRepository @Inject constructor(
    private val lessonDataSource: LessonDataSource,
    private val dataSource: LessonScheduleDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonScheduleRepository {

    override fun getLessonById(lessonId: Long): Flow<Result<Lesson>> = lessonDataSource
        .getLessonById(lessonId)
        .asResultNotNull()

    override suspend fun insertLessonSchedule(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: LessonScheduleType
    ) {
        scope.launch {
            dataSource.insertLessonSchedule(
                lessonId = lessonId,
                date = date,
                startTime = startTime,
                endTime = endTime,
            )
        }
    }

    override suspend fun deleteLessonScheduleById(id: Long) {
        scope.launch {
            dataSource.deleteLessonScheduleById(id)
        }
    }
}