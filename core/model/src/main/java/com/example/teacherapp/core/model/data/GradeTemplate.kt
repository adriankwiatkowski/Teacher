package com.example.teacherapp.core.model.data

import java.math.BigInteger

data class GradeTemplate(
    val id: Long,
    val name: String,
    val description: String?,
    val weight: BigInteger,
)