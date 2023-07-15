package com.example.teacherapp.core.model.data

data class GradeTemplate(
    val id: Long,
    val name: String,
    val description: String?,
    val weight: Int,
)

data class BasicGradeTemplate(
    val id: Long,
    val name: String,
    val weight: Int,
)