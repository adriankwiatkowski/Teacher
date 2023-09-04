package com.example.teacher.core.database.datasource.note

import com.example.teacher.core.model.data.Note
import com.example.teacher.core.model.data.NotePriority
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {

    fun getNotes(): Flow<List<Note>>

    fun getNoteById(id: Long): Flow<Note?>

    suspend fun insertOrUpdateNote(
        id: Long?,
        title: String,
        text: String,
        priority: NotePriority,
    )

    suspend fun deleteNoteById(id: Long)
}