package com.example.teacherapp.core.data.repository.grade

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Grade
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface GradeRepository {

    fun getGradeOrNullById(id: Long): Flow<Result<Grade?>>

    fun getGradesByGradeTemplateId(gradeTemplateId: Long): Flow<Result<List<BasicGradeForTemplate>>>

    fun getGradeTemplateInfoByGradeTemplateId(gradeTemplateId: Long): Flow<Result<GradeTemplateInfo>>

    fun getStudentById(id: Long): Flow<Result<BasicStudent>>

    suspend fun insertOrUpdateGrade(
        id: Long?,
        studentId: Long,
        gradeTemplateId: Long,
        grade: BigDecimal,
    )

    suspend fun deleteGradeById(id: Long)

    suspend fun deleteGradeTemplateById(id: Long)
}