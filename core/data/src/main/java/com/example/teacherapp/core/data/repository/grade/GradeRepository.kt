package com.example.teacherapp.core.data.repository.grade

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import kotlinx.coroutines.flow.Flow

interface GradeRepository {

    fun getGradesByGradeTemplateId(gradeTemplateId: Long): Flow<Result<List<BasicGradeForTemplate>>>
}