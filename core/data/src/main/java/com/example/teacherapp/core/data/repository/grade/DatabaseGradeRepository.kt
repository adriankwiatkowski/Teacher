package com.example.teacherapp.core.data.repository.grade

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.database.datasource.grade.GradeDataSource
import com.example.teacherapp.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseGradeRepository @Inject constructor(
    private val dataSource: GradeDataSource,
    private val gradeTemplateDataSource: GradeTemplateDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : GradeRepository {

    override fun getGradesByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<Result<List<BasicGradeForTemplate>>> = dataSource
        .getGradesByGradeTemplateId(gradeTemplateId)
        .asResult()

    override fun getGradeTemplateInfoByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<Result<GradeTemplateInfo>> = dataSource
        .getGradeTemplateInfoByGradeTemplateId(gradeTemplateId)
        .asResultNotNull()

    override suspend fun deleteGradeById(id: Long) {
        scope.launch {
            dataSource.deleteGradeById(id)
        }
    }

    override suspend fun deleteGradeTemplateById(id: Long) {
        scope.launch {
            gradeTemplateDataSource.deleteGradeTemplateById(id)
        }
    }
}