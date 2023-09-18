package com.example.teacher.core.model.data

import java.math.BigDecimal
import java.time.LocalDate

data class StudentGradesByLesson(
    val studentId: Long,
    val lessonId: Long,
    val lessonName: String,
    val schoolClass: BasicSchoolClass,
    val firstTermGrades: List<StudentGradeInfo>,
    val firstTermAverage: BigDecimal?,
    val secondTermGrades: List<StudentGradeInfo>,
    val secondTermAverage: BigDecimal?,
)

data class StudentGradeInfo(
    val studentId: Long,
    val lessonId: Long,
    val isFirstTerm: Boolean,
    val gradeTemplateId: Long,
    val gradeName: String,
    val grade: StudentGrade?,
    val weight: Long,
    val date: LocalDate,
)

data class StudentGrade(val gradeId: Long, val grade: BigDecimal)