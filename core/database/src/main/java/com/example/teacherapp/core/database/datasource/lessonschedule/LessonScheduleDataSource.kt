package com.example.teacherapp.core.database.datasource.lessonschedule

import com.example.teacherapp.core.database.model.LessonScheduleDto
import com.example.teacherapp.core.model.data.LessonSchedule
import kotlinx.coroutines.flow.Flow

interface LessonScheduleDataSource {

    fun getLessonSchedules(): Flow<List<LessonSchedule>>

    suspend fun insertLessonSchedule(lessonScheduleDtos: List<LessonScheduleDto>)

    suspend fun deleteLessonScheduleById(id: Long)
}