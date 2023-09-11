package com.example.teacher.core.model.data

data class SchoolClass(
    val id: Long,
    val name: String,
    val schoolYear: SchoolYear,
    val students: List<BasicStudent>,
    val lessons: List<BasicLesson>,
)

data class BasicSchoolClass(
    val id: Long,
    val name: String,
    val schoolYear: SchoolYear,
    val studentCount: Long,
    val lessonCount: Long,
)