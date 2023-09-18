package com.example.teacher.core.database.datasource.lesson

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.querymapper.toExternal
import com.example.teacher.core.database.querymapper.toExternalLessons
import com.example.teacher.core.database.querymapper.toExternalLessonsByYear
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonsByYear
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class LessonDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : LessonDataSource {

    private val queries = db.lessonQueries
    private val schoolClassQueries = db.schoolClassQueries

    override fun getLessons(): Flow<List<Lesson>> =
        queries
            .getLessons()
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternalLessons)
            .flowOn(dispatcher)

    override fun getLessonsByYear(): Flow<List<LessonsByYear>> =
        queries
            .getLessons()
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternalLessonsByYear)
            .flowOn(dispatcher)

    override fun getLessonById(id: Long): Flow<Lesson?> =
        queries
            .getLessonById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getLessonsBySchoolClassId(schoolClassId: Long): Flow<List<BasicLesson>> =
        queries
            .getLessonsBySchoolClassId(schoolClassId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> =
        schoolClassQueries
            .getSchoolClassNameById(schoolClassId)
            .asFlow()
            .mapToOneOrNull(dispatcher)

    override suspend fun insertOrUpdateLesson(
        id: Long?,
        schoolClassId: Long,
        name: String,
    ): Unit = withContext(dispatcher) {
        if (id == null) {
            queries.insertLesson(
                id = null,
                school_class_id = schoolClassId,
                name = name,
            )
        } else {
            queries.updateLesson(
                id = id,
                name = name,
            )
        }
    }

    override suspend fun deleteLessonById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteLessonById(id)
    }
}