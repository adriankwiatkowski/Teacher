package com.example.teacherapp.core.database.datasource.grade

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

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
}