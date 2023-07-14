package com.example.teacherapp.core.data.repository.lesson

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonRepository {

    fun getLessonById(lessonId: Long): Flow<Result<Lesson?>>

    fun getSchoolClassNameById(schoolClassId: Long): Flow<String?>

    suspend fun insertOrUpdateLesson(id: Long?, schoolClassId: Long, name: String): Boolean
}