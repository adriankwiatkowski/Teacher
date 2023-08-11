package com.example.teacherapp.core.database.datasource.lesson

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternalLessons
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.LessonWithSchoolYear
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

    override fun getLessonById(id: Long): Flow<Lesson?> =
        queries
            .getLessonById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getLessonWithSchoolYearById(lessonId: Long): Flow<LessonWithSchoolYear?> =
        queries
            .getLessonWithSchoolYearById(lessonId)
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