package com.example.teacher.core.data.repository.grade

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.common.result.asResultNotNull
import com.example.teacher.core.database.datasource.grade.GradeDataSource
import com.example.teacher.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.Grade
import com.example.teacher.core.model.data.GradeTemplateInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

internal class DatabaseGradeRepository @Inject constructor(
    private val dataSource: GradeDataSource,
    private val gradeTemplateDataSource: GradeTemplateDataSource,
    private val studentDataSource: StudentDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : GradeRepository {

    override fun getGradeOrNullById(id: Long): Flow<Result<Grade?>> = dataSource
        .getGradeById(id)
        .asResult()

    override fun getGradesByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<Result<List<BasicGradeForTemplate>>> = dataSource
        .getGradesByGradeTemplateId(gradeTemplateId)
        .asResult()

    override fun getGradeTemplateInfoByGradeTemplateId(
        gradeTemplateId: Long
    ): Flow<Result<GradeTemplateInfo>> = dataSource
        .getGradeTemplateInfoByGradeTemplateId(gradeTemplateId)
        .asResultNotNull()

    override fun getStudentById(id: Long): Flow<Result<BasicStudent>> = studentDataSource
        .getBasicStudentById(id)
        .asResultNotNull()

    override suspend fun insertOrUpdateGrade(
        id: Long?,
        studentId: Long,
        gradeTemplateId: Long,
        grade: BigDecimal,
    ) {
        scope.launch {
            dataSource.insertOrUpdateGrade(
                id = id,
                studentId = studentId,
                gradeTemplateId = gradeTemplateId,
                grade = grade,
            )
        }
    }

    override suspend fun deleteGradeById(id: Long) {
        scope.launch {
            dataSource.deleteGradeById(id)
        }
    }

    override suspend fun deleteGradeTemplateById(id: Long) {
        scope.launch {
            gradeTemplateDataSource.deleteGradeTemplateById(id)
        }
    }
}