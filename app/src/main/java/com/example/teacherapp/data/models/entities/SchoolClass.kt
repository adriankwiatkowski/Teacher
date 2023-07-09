package com.example.teacherapp.data.models.entities

import androidx.compose.runtime.MutableState

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
    val studentCount: Int,
)

data class ExpandableBasicSchoolClasses(
    val expanded: MutableState<Boolean>,
    val schoolClasses: List<BasicSchoolClass>,
)