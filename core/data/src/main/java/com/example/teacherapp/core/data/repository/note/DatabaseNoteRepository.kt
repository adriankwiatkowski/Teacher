package com.example.teacherapp.core.data.repository.note

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.notNull
import com.example.teacherapp.core.database.datasource.note.NoteDataSource
import com.example.teacherapp.core.model.data.Note
import com.example.teacherapp.core.model.data.NotePriority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseNoteRepository @Inject constructor(
    private val dataSource: NoteDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : NoteRepository {

    override fun getNotes(): Flow<Result<List<Note>>> = dataSource
        .getNotes()
        .asResult()

    override fun getNoteOrNullById(id: Long): Flow<Result<Note?>> = dataSource
        .getNoteById(id)
        .asResult()

    override fun getNoteById(id: Long): Flow<Result<Note>> = getNoteOrNullById(id)
        .notNull()

    override suspend fun insertOrUpdateNote(
        id: Long?,
        title: String,
        text: String,
        priority: NotePriority
    ) {
        scope.launch {
            dataSource.insertOrUpdateNote(
                id = id,
                title = title,
                text = text,
                priority = priority,
            )
        }
    }

    override suspend fun deleteNoteById(id: Long) {
        scope.launch {
            dataSource.deleteNoteById(id)
        }
    }
}