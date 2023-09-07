package com.example.teacher.core.model.data

import java.math.BigDecimal
import java.time.LocalDate

data class StudentGradesByLesson(
    val studentId: Long,
    val lessonId: Long,
    val lessonName: String,
    val firstTermGrades: List<StudentGrade>,
    val firstTermAverage: BigDecimal?,
    val secondTermGrades: List<StudentGrade>,
    val secondTermAverage: BigDecimal?,
)

data class StudentGrade(
    val studentId: Long,
    val lessonId: Long,
    val isFirstTerm: Boolean,
    val gradeTemplateId: Long,
    val gradeName: String,
    val gradeId: Long,
    val grade: BigDecimal,
    val weight: Long,
    val date: LocalDate,
)