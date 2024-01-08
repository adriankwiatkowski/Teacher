package com.example.teacher.core.database.datasource.grade

import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.Grade
import com.example.teacher.core.model.data.GradeTemplateInfo
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface GradeDataSource {

    fun getGradeById(id: Long): Flow<Grade?>

    fun getGradesByGradeTemplateId(gradeTemplateId: Long): Flow<List<BasicGradeForTemplate>>

    fun getGradeTemplateInfoByGradeTemplateId(gradeTemplateId: Long): Flow<GradeTemplateInfo?>

    suspend fun upsertGrade(
        id: Long?,
        studentId: Long,
        gradeTemplateId: Long,
        grade: BigDecimal,
    )

    suspend fun deleteGradeById(id: Long)
}