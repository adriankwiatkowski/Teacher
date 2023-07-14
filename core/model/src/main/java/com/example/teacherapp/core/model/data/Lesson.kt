package com.example.teacherapp.core.model.data

data class Lesson(
    val id: Long,
    val name: String,
    val basicSchoolClass: BasicSchoolClass,
)

data class BasicLesson(
    val id: Long,
    val name: String,
    val schoolClassId: Long,
)