package com.example.teacherapp.core.data.repository.lessonnote

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicLessonNote
import com.example.teacherapp.core.model.data.LessonNote
import kotlinx.coroutines.flow.Flow

interface LessonNoteRepository {

    fun getLessonNotesByLessonId(lessonId: Long): Flow<Result<List<BasicLessonNote>>>

    fun getLessonNoteOrNullById(id: Long): Flow<Result<LessonNote?>>

    suspend fun insertOrUpdateLessonNote(
        id: Long?,
        lessonId: Long,
        title: String,
        text: String,
    )

    suspend fun deleteLessonNoteById(id: Long)
}