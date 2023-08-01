package com.example.teacherapp.core.database.datasource.lessonschedule

import java.time.LocalDate
import java.time.LocalTime

interface LessonScheduleDataSource {

    suspend fun insertLessonSchedule(
        lessonId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
    )

    suspend fun deleteLessonScheduleById(id: Long)
}