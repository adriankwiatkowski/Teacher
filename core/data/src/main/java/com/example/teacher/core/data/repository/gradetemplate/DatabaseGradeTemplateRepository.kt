package com.example.teacher.core.data.repository.gradetemplate

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.common.result.asResultNotNull
import com.example.teacher.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.core.model.data.GradeTemplate
import com.example.teacher.core.model.data.Lesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseGradeTemplateRepository @Inject constructor(
    private val dataSource: GradeTemplateDataSource,
    private val lessonDataSource: LessonDataSource,
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

    override fun getLessonById(lessonId: Long): Flow<Result<Lesson>> = lessonDataSource
        .getLessonById(lessonId)
        .asResultNotNull()

    override suspend fun upsertGradeTemplate(
        id: Long?,
        name: String,
        description: String?,
        weight: Int,
        isFirstTerm: Boolean,
        lessonId: Long,
    ) {
        scope.launch {
            dataSource.upsertGradeTemplate(
                id = id,
                name = name,
                description = description,
                weight = weight,
                isFirstTerm = isFirstTerm,
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