package com.example.teacherapp.data.models.entities

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