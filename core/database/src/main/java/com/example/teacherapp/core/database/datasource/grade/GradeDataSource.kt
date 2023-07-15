package com.example.teacherapp.core.database.datasource.grade

import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import kotlinx.coroutines.flow.Flow

interface GradeDataSource {

    fun getGradesByGradeTemplateId(gradeTemplateId: Long): Flow<List<BasicGradeForTemplate>>
}