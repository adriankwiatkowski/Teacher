package com.example.teacher.core.database.datasource.lessonactivity

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.querymapper.toExternal
import com.example.teacher.core.model.data.LessonActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class LessonActivityDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : LessonActivityDataSource {

    private val queries = db.lessonActivityQueries

    override fun getLessonActivitiesByLessonId(lessonId: Long): Flow<List<LessonActivity>> =
        queries
            .getLessonActivitiesByLessonId(lessonId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateLessonActivity(
        id: Long?,
        lessonId: Long,
        studentId: Long,
        sum: Long,
        isFirstTerm: Boolean,
    ): Unit = withContext(dispatcher) {
        if (id == null) {
            queries.insertLessonActivity(
                id = null,
                lesson_id = lessonId,
                student_id = studentId,
                sum = sum,
                is_first_term = isFirstTerm,
            )
        } else {
            queries.updateLessonActivity(
                id = id,
                sum = sum,
            )
        }
    }
}