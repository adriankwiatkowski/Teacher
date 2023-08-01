package com.example.teacherapp.core.database.datasource.lessonschedule

import com.example.teacherapp.core.model.data.LessonSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

interface LessonScheduleDataSource {

    fun getLessonSchedules(): Flow<List<LessonSchedule>>

    suspend fun insertLessonSchedule(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
    )

    suspend fun deleteLessonScheduleById(id: Long)
}