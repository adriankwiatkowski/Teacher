package com.example.teacherapp.core.data.repository.grade

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.database.datasource.grade.GradeDataSource
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseGradeRepository @Inject constructor(
    private val dataSource: GradeDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : GradeRepository {

    override fun getGradesByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<Result<List<BasicGradeForTemplate>>> = dataSource
        .getGradesByGradeTemplateId(gradeTemplateId)
        .asResult()
}