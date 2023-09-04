package com.example.teacher.core.database.datasource.lesson

import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonWithSchoolYear
import kotlinx.coroutines.flow.Flow

interface LessonDataSource {

    fun getLessons(): Flow<List<Lesson>>

    fun getLessonById(id: Long): Flow<Lesson?>

    fun getLessonWithSchoolYearById(lessonId: Long): Flow<LessonWithSchoolYear?>

    fun getLessonsBySchoolClassId(schoolClassId: Long): Flow<List<BasicLesson>>

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?>

    suspend fun insertOrUpdateLesson(
        id: Long?,
        schoolClassId: Long,
        name: String,
    )

    suspend fun deleteLessonById(id: Long)
}