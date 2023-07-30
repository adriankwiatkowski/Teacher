package com.example.teacherapp.core.database.datasource.lessonnote

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicLessonNote
import com.example.teacherapp.core.model.data.LessonNote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class LessonNoteDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : LessonNoteDataSource {

    private val queries = db.lessonNoteQueries

    override fun getLessonNotesByLessonId(lessonId: Long): Flow<List<BasicLessonNote>> =
        queries
            .getLessonNotesByLessonId(lessonId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getLessonNoteById(id: Long): Flow<LessonNote?> =
        queries
            .getLessonNoteById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateLessonNote(
        id: Long?,
        lessonId: Long,
        title: String,
        text: String
    ): Unit = withContext(dispatcher) {
        if (id == null) {
            queries.insertLessonNote(
                id = null,
                lesson_id = lessonId,
                title = title,
                text = text,
            )
        } else {
            queries.updateLessonNote(
                id = id,
                lesson_id = lessonId,
                title = title,
                text = text,
            )
        }
    }

    override suspend fun deleteLessonNoteById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteLessonNoteById(id)
    }
}