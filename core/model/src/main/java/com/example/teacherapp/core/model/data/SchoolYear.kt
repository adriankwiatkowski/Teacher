package com.example.teacherapp.core.model.data

data class SchoolYear(
    val id: Long,
    val name: String,
    val firstTerm: Term,
    val secondTerm: Term,
)