package com.example.teacher.core.database.datasource.grade

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.datasource.utils.querymapper.toExternal
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.Grade
import com.example.teacher.core.model.data.GradeTemplateInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal

internal class GradeDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GradeDataSource {

    private val queries = db.gradeQueries

    override fun getGradeById(id: Long): Flow<Grade?> =
        queries
            .getGradeById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getGradesByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<List<BasicGradeForTemplate>> =
        queries
            .getGradesByGradeTemplateId(gradeTemplateId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getGradeTemplateInfoByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<GradeTemplateInfo?> =
        queries
            .getGradeTemplateInfoByGradeTemplateId(gradeTemplateId)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateGrade(
        id: Long?,
        studentId: Long,
        gradeTemplateId: Long,
        grade: BigDecimal,
    ): Unit = withContext(dispatcher) {
        if (id == null) {
            queries.insertGrade(
                id = null,
                student_id = studentId,
                grade_template_id = gradeTemplateId,
                grade = grade,
                date = TimeUtils.currentDate(),
            )
        } else {
            queries.updateGrade(
                id = id,
                grade = grade,
                date = TimeUtils.currentDate(),
            )
        }
    }

    override suspend fun deleteGradeById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteGradeById(id)
    }
}