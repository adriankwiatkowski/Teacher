package com.example.teacher.core.model.data

import java.math.BigDecimal

data class StudentWithAttendance(
    val student: BasicStudent,
    val firstTermAverageAttendancePercentage: BigDecimal,
    val secondTermAverageAttendancePercentage: BigDecimal,
)