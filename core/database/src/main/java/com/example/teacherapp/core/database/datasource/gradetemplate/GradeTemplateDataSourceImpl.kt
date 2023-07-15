package com.example.teacherapp.core.database.datasource.gradetemplate

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.core.model.data.GradeTemplate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate

internal class GradeTemplateDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GradeTemplateDataSource {

    private val queries = db.gradeTemplateQueries

    override fun getGradeTemplateById(id: Long): Flow<GradeTemplate?> =
        queries
            .getGradeTemplateById(id)
            .asFlow()
            .mapToOneOrNull()
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getGradeTemplatesByLessonId(lessonId: Long): Flow<List<BasicGradeTemplate>> =
        queries
            .getGradeTemplatesByLessonId(lessonId)
            .asFlow()
            .mapToList()
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateGradeTemplate(
        id: Long?,
        name: String,
        description: String?,
        weight: Int,
        lessonId: Long,
    ): Unit = withContext(dispatcher) {
        if (id == null) {
            queries.insertGradeTemplate(
                id = null,
                lesson_id = lessonId,
                name = name,
                description = description,
                date = LocalDate.now(),
                weight = weight.toLong(),
            )
        } else {
            queries.updateGradeTemplate(
                id = id,
                lesson_id = lessonId,
                name = name,
                description = description,
                date = LocalDate.now(),
                weight = weight.toLong(),
            )
        }
    }

    override suspend fun deleteGradeTemplateById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteGradeTemplateById(id)
    }
}