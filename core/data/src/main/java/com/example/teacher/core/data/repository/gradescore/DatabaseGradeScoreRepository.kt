package com.example.teacher.core.data.repository.gradescore

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.database.datasource.gradescore.GradeScoreDataSource
import com.example.teacher.core.model.data.GradeScore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseGradeScoreRepository @Inject constructor(
    private val dataSource: GradeScoreDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : GradeScoreRepository {

    override fun getGradeScoreByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<Result<GradeScore?>> = dataSource
        .getGradeScoreByGradeTemplateId(gradeTemplateId)
        .asResult()

    override suspend fun updateGradeScore(gradeScore: GradeScore) {
        scope.launch {
            dataSource.updateGradeScore(gradeScore)
        }
    }
}