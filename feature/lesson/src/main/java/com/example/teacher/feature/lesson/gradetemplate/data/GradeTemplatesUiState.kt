package com.example.teacher.feature.lesson.gradetemplate.data

import com.example.teacher.core.model.data.BasicGradeTemplate

internal data class GradeTemplatesUiState(
    val firstTermGrades: List<BasicGradeTemplate>,
    val secondTermGrades: List<BasicGradeTemplate>,
)