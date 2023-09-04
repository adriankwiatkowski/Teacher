package com.example.teacher.feature.grade.data

import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.GradeTemplateInfo

internal data class GradesUiState(
    val grades: List<BasicGradeForTemplate>,
    val gradeTemplateInfo: GradeTemplateInfo,
)