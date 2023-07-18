package com.example.teacherapp.core.database.datasource.lessonactivity

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.LessonActivity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
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
            .mapToList()
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateLessonActivity(
        id: Long?,
        lessonId: Long,
        studentId: Long,
        sum: Long
    ): Unit = withContext(dispatcher) {
        if (id == null) {
            queries.insertLessonActivity(
                id = null,
                lesson_id = lessonId,
                student_id = studentId,
                sum = sum,
            )
        } else {
            queries.updateLessonActivity(
                id = id,
                sum = sum,
            )
        }
    }
}