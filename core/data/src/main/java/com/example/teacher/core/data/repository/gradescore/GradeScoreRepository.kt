package com.example.teacher.core.data.repository.gradescore

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.GradeScore
import kotlinx.coroutines.flow.Flow

interface GradeScoreRepository {

    fun getGradeScoreByGradeTemplateId(gradeTemplateId: Long): Flow<Result<GradeScore?>>

    suspend fun updateGradeScore(gradeScore: GradeScore)
}