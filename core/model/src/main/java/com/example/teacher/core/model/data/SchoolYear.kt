package com.example.teacher.core.model.data

data class SchoolYear(
    val id: Long,
    val name: String,
    val firstTerm: Term,
    val secondTerm: Term,
)