package com.example.teacherapp.core.database.datasource.note

import com.example.teacherapp.core.model.data.Note
import com.example.teacherapp.core.model.data.NotePriority
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {

    fun getNotes(studentId: Long): Flow<List<Note>>

    fun getNoteById(id: Long): Flow<Note?>

    suspend fun insertOrUpdateNote(
        id: Long?,
        title: String,
        text: String,
        priority: NotePriority,
    )

    suspend fun deleteNoteById(id: Long)
}