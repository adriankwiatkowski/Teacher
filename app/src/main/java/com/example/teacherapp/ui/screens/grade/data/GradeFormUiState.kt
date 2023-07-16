package com.example.teacherapp.ui.screens.grade.data

import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.GradeTemplateInfo

data class GradeFormUiState(
    val gradeTemplateInfo: GradeTemplateInfo,
    val student: BasicStudent,
)