package com.example.teacher.core.data.repository.lessonactivity

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.LessonActivity
import kotlinx.coroutines.flow.Flow

interface LessonActivityRepository {

    fun getLessonActivitiesByLessonId(lessonId: Long): Flow<Result<List<LessonActivity>>>

    suspend fun decreaseLessonActivity(lessonActivity: LessonActivity)

    suspend fun increaseLessonActivity(lessonActivity: LessonActivity)
}