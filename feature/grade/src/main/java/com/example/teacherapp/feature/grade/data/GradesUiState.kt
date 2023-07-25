package com.example.teacherapp.feature.grade.data

import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo

internal data class GradesUiState(
    val grades: List<BasicGradeForTemplate>,
    val gradeTemplateInfo: GradeTemplateInfo,
)