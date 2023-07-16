package com.example.teacherapp.core.database.datasource.grade

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class GradeDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GradeDataSource {

    private val queries = db.gradeQueries

    override fun getGradesByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<List<BasicGradeForTemplate>> =
        queries
            .getGradesByGradeTemplateId(gradeTemplateId)
            .asFlow()
            .mapToList()
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getGradeTemplateInfoByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<GradeTemplateInfo?> =
        queries.getGradeTemplateInfoByGradeTemplateId(gradeTemplateId)
            .asFlow()
            .mapToOneOrNull()
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun deleteGradeById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteGradeById(id)
    }
}