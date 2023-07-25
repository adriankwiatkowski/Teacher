package com.example.teacherapp.feature.grade.data

import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.GradeTemplateInfo

internal data class GradeFormUiState(
    val gradeTemplateInfo: GradeTemplateInfo,
    val student: BasicStudent,
)