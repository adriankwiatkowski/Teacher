package com.example.teacher.core.model.data

data class Lesson(
    val id: Long,
    val name: String,
    val schoolClass: BasicSchoolClass,
)

data class BasicLesson(
    val id: Long,
    val name: String,
    val schoolClassId: Long,
)

data class LessonWithSchoolYear(
    val id: Long,
    val name: String,
    val schoolClassId: Long,
    val schoolClassName: String,
    val schoolYear: SchoolYear,
)