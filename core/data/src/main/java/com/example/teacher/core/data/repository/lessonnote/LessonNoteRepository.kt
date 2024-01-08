package com.example.teacher.core.data.repository.lessonnote

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicLessonNote
import com.example.teacher.core.model.data.LessonNote
import kotlinx.coroutines.flow.Flow

interface LessonNoteRepository {

    fun getLessonNotesByLessonId(lessonId: Long): Flow<Result<List<BasicLessonNote>>>

    fun getLessonNoteOrNullById(id: Long): Flow<Result<LessonNote?>>

    suspend fun upsertLessonNote(id: Long?, lessonId: Long, title: String, text: String)

    suspend fun deleteLessonNoteById(id: Long)
}