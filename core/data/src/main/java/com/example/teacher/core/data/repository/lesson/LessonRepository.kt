package com.example.teacher.core.data.repository.lesson

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonRepository {

    fun getLessons(): Flow<Result<List<Lesson>>>

    fun getLessonOrNullById(lessonId: Long): Flow<Result<Lesson?>>

    fun getLessonById(lessonId: Long): Flow<Result<Lesson>>

    fun getSchoolClassNameById(schoolClassId: Long): Flow<String?>

    suspend fun insertOrUpdateLesson(id: Long?, schoolClassId: Long, name: String): Boolean

    suspend fun deleteLessonById(id: Long)
}