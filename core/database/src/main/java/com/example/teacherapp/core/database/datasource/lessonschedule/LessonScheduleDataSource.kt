package com.example.teacherapp.core.database.datasource.lessonschedule

import com.example.teacherapp.core.database.model.LessonScheduleDto
import com.example.teacherapp.core.model.data.LessonSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface LessonScheduleDataSource {

    fun getLessonSchedules(date: LocalDate): Flow<List<LessonSchedule>>

    fun getLessonScheduleById(lessonScheduleId: Long): Flow<LessonSchedule?>

    suspend fun insertLessonSchedule(lessonScheduleDtos: List<LessonScheduleDto>)

    suspend fun deleteLessonScheduleById(id: Long)
}