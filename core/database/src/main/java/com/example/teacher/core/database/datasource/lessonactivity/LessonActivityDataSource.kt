package com.example.teacher.core.database.datasource.lessonactivity

import com.example.teacher.core.model.data.LessonActivity
import kotlinx.coroutines.flow.Flow

interface LessonActivityDataSource {

    fun getLessonActivitiesByLessonId(lessonId: Long): Flow<List<LessonActivity>>

    suspend fun upsertLessonActivity(
        id: Long?,
        lessonId: Long,
        studentId: Long,
        sum: Long,
        isFirstTerm: Boolean,
    )
}