package com.example.teacher.core.common.utils

import com.example.teacher.core.model.data.GradeWithAverage
import java.math.BigDecimal
import java.math.RoundingMode

object DecimalUtils {

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
        return grade.setScale(2, RoundingMode.HALF_UP).toPlainString()
    }

    fun calculateWeightedAverage(grades: List<GradeWithAverage>): BigDecimal? {
        return if (grades.isNotEmpty()) {
            val weightSum = grades.sumOf { it.weight }

            grades
                .sumOf { it.grade * BigDecimal(it.weight) }
                .safeDivide(BigDecimal(weightSum))
        } else {
            null
        }
    }

    fun calculateArithmeticAverage(numbers: List<BigDecimal>): BigDecimal? {
        return if (numbers.isNotEmpty()) {
            numbers.sumOf { it }.safeDivide(BigDecimal(numbers.size))
        } else {
            null
        }
    }

    fun BigDecimal.safeDivide(divisor: BigDecimal): BigDecimal {
        if (divisor == BigDecimal.ZERO) {
            return this
        }
        return this.divide(divisor, 2, RoundingMode.HALF_UP)
    }

    fun BigDecimal.toPercentage(): BigDecimal = this.times(BigDecimal.valueOf(100L))
}