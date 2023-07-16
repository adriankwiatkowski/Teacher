package com.example.teacherapp.core.data.repository.gradetemplate

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.core.model.data.GradeTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseGradeTemplateRepository @Inject constructor(
    private val dataSource: GradeTemplateDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : GradeTemplateRepository {

    override fun getGradeTemplateOrNullById(id: Long): Flow<Result<GradeTemplate?>> = dataSource
        .getGradeTemplateById(id)
        .asResult()

    override fun getGradeTemplatesByLessonId(
        lessonId: Long
    ): Flow<Result<List<BasicGradeTemplate>>> = dataSource
        .getGradeTemplatesByLessonId(lessonId)
        .asResult()

    override suspend fun insertOrUpdateGradeTemplate(
        id: Long?,
        name: String,
        description: String?,
        weight: Int,
        lessonId: Long,
    ) {
        scope.launch {
            dataSource.insertOrUpdateGradeTemplate(
                id = id,
                name = name,
                description = description,
                weight = weight,
                lessonId = lessonId,
            )
        }
    }

    override suspend fun deleteGradeTemplateById(id: Long) {
        scope.launch {
            dataSource.deleteGradeTemplateById(id)
        }
    }
}