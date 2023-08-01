package com.example.teacherapp.core.data.repository.lessonschedule

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.LessonSchedule
import com.example.teacherapp.core.model.data.LessonScheduleType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

interface LessonScheduleRepository {

    fun getLessonSchedules(): Flow<Result<List<LessonSchedule>>>

    fun getLessonById(lessonId: Long): Flow<Result<Lesson>>

    suspend fun insertLessonSchedule(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        type: LessonScheduleType,
    )

    suspend fun deleteLessonScheduleById(id: Long)
}