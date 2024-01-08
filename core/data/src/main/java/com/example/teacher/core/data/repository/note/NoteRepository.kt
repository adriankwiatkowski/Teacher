package com.example.teacher.core.data.repository.note

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Note
import com.example.teacher.core.model.data.NotePriority
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<Result<List<Note>>>

    fun getNoteOrNullById(id: Long): Flow<Result<Note?>>

    fun getNoteById(id: Long): Flow<Result<Note>>

    suspend fun upsertNote(id: Long?, title: String, text: String, priority: NotePriority)

    suspend fun deleteNoteById(id: Long)
}