package com.example.teacher.core.database.datasource.lesson

import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonsByYear
import com.example.teacher.core.model.data.SchoolYear
import kotlinx.coroutines.flow.Flow

interface LessonDataSource {

    fun getLessons(): Flow<List<Lesson>>

    fun getLessonsByYear(): Flow<List<LessonsByYear>>

    fun getLessonById(id: Long): Flow<Lesson?>

    fun getLessonsBySchoolClassId(schoolClassId: Long): Flow<List<BasicLesson>>

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?>

    fun getSchoolYearByLessonId(lessonId: Long): Flow<SchoolYear?>

    suspend fun insertOrUpdateLesson(
        id: Long?,
        schoolClassId: Long,
        name: String,
    )

    suspend fun deleteLessonById(id: Long)
}