package com.example.teacherapp.data.models.entities

import java.math.BigDecimal
import java.time.LocalDate

data class Grade(
    val id: Long,

    val studentId: Long,
    val lesson: Lesson,

    val name: String,
    val description: String?,

    val grade: BigDecimal,
    val weight: Int,

    val date: LocalDate,
)