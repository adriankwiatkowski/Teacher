package com.example.teacher.core.data.repository.gradetemplate

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.core.model.data.GradeTemplate
import kotlinx.coroutines.flow.Flow

interface GradeTemplateRepository {

    fun getGradeTemplateOrNullById(id: Long): Flow<Result<GradeTemplate?>>

    fun getGradeTemplatesByLessonId(lessonId: Long): Flow<Result<List<BasicGradeTemplate>>>

    suspend fun insertOrUpdateGradeTemplate(
        id: Long?,
        name: String,
        description: String?,
        weight: Int,
        lessonId: Long,
    )

    suspend fun deleteGradeTemplateById(id: Long)
}