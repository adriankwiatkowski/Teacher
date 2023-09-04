package com.example.teacher.core.database.datasource.lessonactivity

import com.example.teacher.core.model.data.LessonActivity
import kotlinx.coroutines.flow.Flow

interface LessonActivityDataSource {

    fun getLessonActivitiesByLessonId(lessonId: Long): Flow<List<LessonActivity>>

    suspend fun insertOrUpdateLessonActivity(id: Long?, lessonId: Long, studentId: Long, sum: Long)
}