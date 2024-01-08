package com.example.teacher.core.database.datasource.note

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.querymapper.toExternal
import com.example.teacher.core.model.data.Note
import com.example.teacher.core.model.data.NotePriority
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class NoteDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : NoteDataSource {

    private val queries = db.noteQueries

    override fun getNotes(): Flow<List<Note>> = queries
        .getNotes()
        .asFlow()
        .mapToList(dispatcher)
        .map(::toExternal)
        .flowOn(dispatcher)

    override fun getNoteById(id: Long): Flow<Note?> = queries
        .getNoteById(id)
        .asFlow()
        .mapToOneOrNull(dispatcher)
        .map(::toExternal)
        .flowOn(dispatcher)

    override suspend fun upsertNote(
        id: Long?,
        title: String,
        text: String,
        priority: NotePriority
    ): Unit = withContext(dispatcher) {
        if (id == null) {
            queries.insertNote(
                id = null,
                title = title,
                text = text,
                priority = priority.priority,
            )
        } else {
            queries.updateNote(
                id = id,
                title = title,
                text = text,
                priority = priority.priority
            )
        }
    }

    override suspend fun deleteNoteById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteNoteById(id)
    }
}