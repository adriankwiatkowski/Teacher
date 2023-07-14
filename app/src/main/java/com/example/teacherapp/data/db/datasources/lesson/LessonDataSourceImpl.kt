package com.example.teacherapp.data.db.datasources.lesson

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.utils.querymappers.LessonMapper
import com.example.teacherapp.data.di.DefaultDispatcher
import com.example.teacherapp.data.models.entities.BasicLesson
import com.example.teacherapp.data.models.entities.Lesson
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LessonDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : LessonDataSource {

    private val queries = db.lessonQueries
    private val schoolClassQueries = db.schoolClassQueries

    override fun getLessonById(id: Long): Flow<Lesson?> =
        queries
            .getLessonById(id, LessonMapper::mapLesson)
            .asFlow()
            .mapToOneOrNull()

    override fun getLessonsBySchoolClassId(schoolClassId: Long): Flow<List<BasicLesson>> =
        queries
            .getLessonsBySchoolClassId(schoolClassId, LessonMapper::mapBasicLesson)
            .asFlow()
            .mapToList()

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> =
        schoolClassQueries.getSchoolClassNameById(schoolClassId)
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
}