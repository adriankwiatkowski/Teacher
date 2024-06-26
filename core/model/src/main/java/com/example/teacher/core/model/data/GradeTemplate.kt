package com.example.teacher.core.model.data

import java.math.BigDecimal

data class GradeTemplate(
    val id: Long,
    val lessonId: Long,
    val name: String,
    val description: String?,
    val weight: Int,
    val isFirstTerm: Boolean,
)

data class BasicGradeTemplate(
    val id: Long,
    val lessonId: Long,
    val name: String,
    val weight: Int,
    val isFirstTerm: Boolean,
    val averageGrade: BigDecimal?,
)

data class GradeTemplateInfo(
    val gradeTemplateId: Long,
    val gradeName: String,
    val gradeDescription: String?,
    val gradeWeight: Int,
    val isFirstTerm: Boolean,
    val averageGrade: BigDecimal?,
    val lesson: Lesson,
)