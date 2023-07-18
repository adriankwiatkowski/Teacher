package com.example.teacherapp.core.data.repository.lessonactivity

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.database.datasource.lessonactivity.LessonActivityDataSource
import com.example.teacherapp.core.model.data.LessonActivity
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
        scope.launch {
            val sum = lessonActivity.sum
            val newSum = if (sum != null) sum - 1L else -1L

            dataSource.insertOrUpdateLessonActivity(
                id = lessonActivity.id,
                lessonId = lessonActivity.lesson.id,
                studentId = lessonActivity.student.id,
                sum = newSum,
            )
        }
    }

    override suspend fun increaseLessonActivity(lessonActivity: LessonActivity) {
        scope.launch {
            val sum = lessonActivity.sum
            val newSum = if (sum != null) sum + 1L else 1L

            dataSource.insertOrUpdateLessonActivity(
                id = lessonActivity.id,
                lessonId = lessonActivity.lesson.id,
                studentId = lessonActivity.student.id,
                sum = newSum,
            )
        }
    }
}