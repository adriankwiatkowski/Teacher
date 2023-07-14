package com.example.teacherapp.core.model.data

import java.time.LocalDate

data class Term(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)