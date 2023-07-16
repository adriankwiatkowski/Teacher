package com.example.teacherapp.core.data.repository.grade

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import kotlinx.coroutines.flow.Flow

interface GradeRepository {

    fun getGradesByGradeTemplateId(gradeTemplateId: Long): Flow<Result<List<BasicGradeForTemplate>>>

    fun getGradeTemplateInfoByGradeTemplateId(gradeTemplateId: Long): Flow<Result<GradeTemplateInfo>>

    suspend fun deleteGradeById(id: Long)

    suspend fun deleteGradeTemplateById(id: Long)
}