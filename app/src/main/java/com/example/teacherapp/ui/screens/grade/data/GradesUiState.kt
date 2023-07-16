package com.example.teacherapp.ui.screens.grade.data

import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo

data class GradesUiState(
    val grades: List<BasicGradeForTemplate>,
    val gradeTemplateInfo: GradeTemplateInfo,
)