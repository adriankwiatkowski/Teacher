package com.example.teacher.core.model.data

data class SchoolClassesByYear(
    val year: SchoolYear,
    val schoolClasses: List<BasicSchoolClass>,
)