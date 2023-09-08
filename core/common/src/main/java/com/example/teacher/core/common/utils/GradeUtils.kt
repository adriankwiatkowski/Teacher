package com.example.teacher.core.common.utils

import java.math.BigDecimal

object GradeUtils {

    val One = BigDecimal("1.00")

    val TwoMinus = BigDecimal("1.75")
    val Two = BigDecimal("2.00")
    val TwoPlus = BigDecimal("2.50")

    val ThreeMinus = BigDecimal("2.75")
    val Three = BigDecimal("3.00")
    val ThreePlus = BigDecimal("3.50")

    val FourMinus = BigDecimal("3.75")
    val Four = BigDecimal("4.00")
    val FourPlus = BigDecimal("4.50")

    val FiveMinus = BigDecimal("4.75")
    val Five = BigDecimal("5.00")
    val FivePlus = BigDecimal("5.50")

    val SixMinus = BigDecimal("5.75")
    val Six = BigDecimal("6.00")

    val MinGrade = BigDecimal("1.00")
    val MaxGrade = BigDecimal("6.00")

    fun toGrade(grade: BigDecimal): String {
        return when (grade) {
            One -> "1"

            TwoMinus -> "2-"
            Two -> "2"
            TwoPlus -> "2+"

            ThreeMinus -> "3-"
            Three -> "3"
            ThreePlus -> "3+"

            FourMinus -> "4-"
            Four -> "4"
            FourPlus -> "4+"

            FiveMinus -> "5-"
            Five -> "5"
            FivePlus -> "5+"

            SixMinus -> "6-"
            Six -> "6"

            else -> throw IllegalArgumentException()
        }
    }

    fun toLiteral(grade: BigDecimal): String {
        // TODO: Change it to behave like school average round up/down.
        return grade.toPlainString()
    }

    fun calculateAverage(grades: List<BigDecimal>): BigDecimal? {
        return if (grades.isNotEmpty()) {
            grades.sumOf { it }.divide(BigDecimal(grades.size.toLong()))
        } else {
            null
        }
    }
}