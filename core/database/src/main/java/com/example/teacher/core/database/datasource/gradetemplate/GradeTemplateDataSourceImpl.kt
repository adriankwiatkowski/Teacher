package com.example.teacher.core.database.datasource.gradetemplate

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.querymapper.toExternal
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.core.model.data.GradeTemplate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class GradeTemplateDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GradeTemplateDataSource {

    private val queries = db.gradeTemplateQueries

    override fun getGradeTemplateById(id: Long): Flow<GradeTemplate?> =
        queries
            .getGradeTemplateById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getGradeTemplatesByLessonId(lessonId: Long): Flow<List<BasicGradeTemplate>> =
        queries
            .getGradeTemplatesByLessonId(lessonId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateGradeTemplate(
        id: Long?,
        name: String,
        description: String?,
        weight: Int,
        isFirstTerm: Boolean,
        lessonId: Long,
    ): Unit = withContext(dispatcher) {
        val actualDescription = if (description.isNullOrBlank()) null else description

        if (id == null) {
            queries.insertGradeTemplate(
                id = null,
                lesson_id = lessonId,
                name = name,
                description = actualDescription,
                date = TimeUtils.currentDate(),
                weight = weight.toLong(),
                is_first_term = isFirstTerm,
            )
        } else {
            queries.updateGradeTemplate(
                id = id,
                lesson_id = lessonId,
                name = name,
                description = actualDescription,
                date = TimeUtils.currentDate(),
                weight = weight.toLong(),
                is_first_term = isFirstTerm,
            )
        }
    }

    override suspend fun deleteGradeTemplateById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteGradeTemplateById(id)
    }
}