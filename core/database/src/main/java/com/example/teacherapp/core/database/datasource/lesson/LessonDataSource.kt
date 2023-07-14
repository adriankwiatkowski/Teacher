package com.example.teacherapp.core.database.datasource.lesson

import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.Lesson
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