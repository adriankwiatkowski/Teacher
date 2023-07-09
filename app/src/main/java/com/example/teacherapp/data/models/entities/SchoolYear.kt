package com.example.teacherapp.data.models.entities

data class SchoolYear(
    val id: Long,
    val name: String,
    val firstTerm: Term,
    val secondTerm: Term,
)