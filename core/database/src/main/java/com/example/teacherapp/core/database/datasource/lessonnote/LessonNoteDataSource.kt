package com.example.teacherapp.core.database.datasource.lessonnote

import com.example.teacherapp.core.model.data.BasicLessonNote
import com.example.teacherapp.core.model.data.LessonNote
import kotlinx.coroutines.flow.Flow

interface LessonNoteDataSource {

    fun getLessonNotesByLessonId(lessonId: Long): Flow<List<BasicLessonNote>>

    fun getLessonNoteById(id: Long): Flow<LessonNote?>

    suspend fun insertOrUpdateLessonNote(
        id: Long?,
        lessonId: Long,
        title: String,
        text: String,
    )

    suspend fun deleteLessonNoteById(id: Long)
}