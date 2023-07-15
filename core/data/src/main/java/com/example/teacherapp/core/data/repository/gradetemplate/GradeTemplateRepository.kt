package com.example.teacherapp.core.data.repository.gradetemplate

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.core.model.data.GradeTemplate
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