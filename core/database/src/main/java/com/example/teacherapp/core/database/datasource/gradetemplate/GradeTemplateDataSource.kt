package com.example.teacherapp.core.database.datasource.gradetemplate

import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.core.model.data.GradeTemplate
import kotlinx.coroutines.flow.Flow

interface GradeTemplateDataSource {

    fun getGradeTemplateById(id: Long): Flow<GradeTemplate?>

    fun getGradeTemplatesByLessonId(lessonId: Long): Flow<List<BasicGradeTemplate>>

    suspend fun insertOrUpdateGradeTemplate(
        id: Long?,
        name: String,
        description: String?,
        weight: Int,
        lessonId: Long,
    )

    suspend fun deleteGradeTemplateById(id: Long)
}