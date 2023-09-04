package com.example.teacher.feature.grade.data

import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.GradeTemplateInfo

internal data class GradeFormUiState(
    val gradeTemplateInfo: GradeTemplateInfo,
    val student: BasicStudent,
)