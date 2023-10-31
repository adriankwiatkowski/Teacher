package com.example.teacher.core.database.datasource.gradescore

import com.example.teacher.core.model.data.GradeScore
import kotlinx.coroutines.flow.Flow

interface GradeScoreDataSource {

    fun getGradeScoreByGradeTemplateId(gradeTemplateId: Long): Flow<GradeScore?>

    suspend fun updateGradeScore(gradeScore: GradeScore)
}