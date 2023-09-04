package com.example.teacher.core.database.datasource.gradetemplate

import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.core.model.data.GradeTemplate
import kotlinx.coroutines.flow.Flow

interface GradeTemplateDataSource {

    fun getGradeTemplateById(id: Long): Flow<GradeTemplate?>

    fun getGradeTemplatesByLessonId(lessonId: Long): Flow<List<BasicGradeTemplate>>

    suspend fun insertOrUpdateGradeTemplate(
        id: Long?,
        name: String,
        description: String?,
        weight: Int,
        isFirstTerm: Boolean,
        lessonId: Long,
    )

    suspend fun deleteGradeTemplateById(id: Long)
}