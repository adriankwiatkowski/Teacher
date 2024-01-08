package com.example.teacher.core.database.datasource.lessonnote

import com.example.teacher.core.model.data.BasicLessonNote
import com.example.teacher.core.model.data.LessonNote
import kotlinx.coroutines.flow.Flow

interface LessonNoteDataSource {

    fun getLessonNotesByLessonId(lessonId: Long): Flow<List<BasicLessonNote>>

    fun getLessonNoteById(id: Long): Flow<LessonNote?>

    suspend fun upsertLessonNote(id: Long?, lessonId: Long, title: String, text: String)

    suspend fun deleteLessonNoteById(id: Long)
}