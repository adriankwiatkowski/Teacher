package com.example.teacher.core.model.data

data class GradeTemplate(
    val id: Long,
    val lessonId: Long,
    val name: String,
    val description: String?,
    val weight: Int,
)

data class BasicGradeTemplate(
    val id: Long,
    val lessonId: Long,
    val name: String,
    val weight: Int,
)

data class GradeTemplateInfo(
    val gradeTemplateId: Long,
    val gradeName: String,
    val gradeWeight: Int,
    val lessonId: Long,
    val lessonName: String,
    val schoolClassId: Long,
    val schoolClassName: String,
)