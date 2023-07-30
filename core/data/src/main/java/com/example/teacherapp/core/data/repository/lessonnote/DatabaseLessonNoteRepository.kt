package com.example.teacherapp.core.data.repository.lessonnote

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.database.datasource.lessonnote.LessonNoteDataSource
import com.example.teacherapp.core.model.data.BasicLessonNote
import com.example.teacherapp.core.model.data.LessonNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseLessonNoteRepository @Inject constructor(
    private val dataSource: LessonNoteDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonNoteRepository {

    override fun getLessonNotesByLessonId(
        lessonId: Long
    ): Flow<Result<List<BasicLessonNote>>> = dataSource
        .getLessonNotesByLessonId(lessonId)
        .asResult()

    override fun getLessonNoteOrNullById(id: Long): Flow<Result<LessonNote?>> = dataSource
        .getLessonNoteById(id)
        .asResult()

    override suspend fun insertOrUpdateLessonNote(
        id: Long?,
        lessonId: Long,
        title: String,
        text: String
    ) {
        scope.launch {
            dataSource.insertOrUpdateLessonNote(
                id = id,
                lessonId = lessonId,
                title = title,
                text = text,
            )
        }
    }

    override suspend fun deleteLessonNoteById(id: Long) {
        scope.launch {
            dataSource.deleteLessonNoteById(id)
        }
    }
}