package com.example.teacherapp.data.models.entities

import java.time.LocalDate

data class Term(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)