package com.example.teacherapp.core.model.data

import java.math.BigDecimal
import java.time.LocalDate

data class StudentGradesByLesson(
    val studentId: Long,
    val lessonId: Long,
    val lessonName: String,
    val average: BigDecimal,
    val gradesByLessonId: List<StudentGrade>,
)

data class StudentGrade(
    val studentId: Long,
    val lessonId: Long,
    val gradeTemplateId: Long,
    val gradeName: String,
    val gradeId: Long,
    val grade: BigDecimal,
    val weight: Long,
    val date: LocalDate,
)