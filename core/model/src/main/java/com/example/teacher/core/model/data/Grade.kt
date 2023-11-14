package com.example.teacher.core.model.data

import java.math.BigDecimal
import java.time.LocalDate

data class Grade(
    val id: Long,
    val grade: BigDecimal,
    val date: LocalDate,

    val studentId: Long,
    val studentFullName: String,

    val lessonId: Long,
    val lessonName: String,

    val schoolClassId: Long,
    val schoolClassName: String,

    val gradeTemplate: GradeTemplate,
)

data class BasicGrade(
    val id: Long,
    val grade: BigDecimal,
    val date: LocalDate,

    val studentId: Long,
    val lessonId: Long,

    val name: String,
    val weight: Int,
    val isFirstTerm: Boolean,
)

data class BasicGradeForTemplate(
    val id: Long?,
    val grade: BigDecimal?,
    val date: LocalDate?,

    val studentId: Long,
    val studentFullName: String,

    val gradeName: String,
    val gradeWeight: Int,
    val isFirstTerm: Boolean,
)

data class GradeWithWeight(val grade: BigDecimal, val weight: Int)