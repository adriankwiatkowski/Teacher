package com.example.teacher.core.data.repository.lessonactivity

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.database.datasource.lessonactivity.LessonActivityDataSource
import com.example.teacher.core.model.data.LessonActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseLessonActivityRepository @Inject constructor(
    private val dataSource: LessonActivityDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonActivityRepository {

    override fun getLessonActivitiesByLessonId(
        lessonId: Long
    ): Flow<Result<List<LessonActivity>>> = dataSource
        .getLessonActivitiesByLessonId(lessonId)
        .asResult()

    override suspend fun decreaseLessonActivity(lessonActivity: LessonActivity) {
        val sum = lessonActivity.sum
        val newSum = if (sum != null) sum - 1L else -1L

        upsertLessonActivity(lessonActivity = lessonActivity, newSum = newSum)
    }

    override suspend fun increaseLessonActivity(lessonActivity: LessonActivity) {
        val sum = lessonActivity.sum
        val newSum = if (sum != null) sum + 1L else 1L

        upsertLessonActivity(lessonActivity = lessonActivity, newSum = newSum)
    }

    private suspend fun upsertLessonActivity(lessonActivity: LessonActivity, newSum: Long) {
        scope.launch {
            dataSource.upsertLessonActivity(
                id = lessonActivity.id,
                lessonId = lessonActivity.lesson.id,
                studentId = lessonActivity.student.id,
                sum = newSum,
                isFirstTerm = lessonActivity.isFirstTerm,
            )
        }
    }
}