package com.example.teacherapp.core.database.datasource.lesson

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.Lesson
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
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

    override fun getLessonById(id: Long): Flow<Lesson?> =
        queries
            .getLessonById(id)
            .asFlow()
            .mapToOneOrNull()
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getLessonsBySchoolClassId(schoolClassId: Long): Flow<List<BasicLesson>> =
        queries
            .getLessonsBySchoolClassId(schoolClassId)
            .asFlow()
            .mapToList()
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> =
        schoolClassQueries
            .getSchoolClassNameById(schoolClassId)
            .asFlow()
            .mapToOneOrNull()

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