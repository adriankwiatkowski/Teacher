package com.example.teacherapp.core.database.datasource.grade

import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import kotlinx.coroutines.flow.Flow

interface GradeDataSource {

    fun getGradesByGradeTemplateId(gradeTemplateId: Long): Flow<List<BasicGradeForTemplate>>

    fun getGradeTemplateInfoByGradeTemplateId(gradeTemplateId: Long): Flow<GradeTemplateInfo?>

    suspend fun deleteGradeById(id: Long)
}