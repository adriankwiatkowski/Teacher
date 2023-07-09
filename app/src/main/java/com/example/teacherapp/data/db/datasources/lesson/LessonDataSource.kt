package com.example.teacherapp.data.db.datasources.lesson

import com.example.teacherapp.data.models.entities.BasicLesson
import com.example.teacherapp.data.models.entities.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonDataSource {

    fun getLessonById(id: Long): Flow<Lesson?>

    fun getLessonsBySchoolClassId(schoolClassId: Long): Flow<List<BasicLesson>>

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?>

    suspend fun insertOrUpdateLesson(
        id: Long?,
        schoolClassId: Long,
        name: String,
    )
}