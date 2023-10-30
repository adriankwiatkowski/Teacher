package com.example.teacher.feature.grade.data

import com.example.teacher.core.ui.model.InputField
import java.math.BigDecimal

internal data class GradeScoreData(
    val maxScore: InputField<Int?>,
    val studentScore: InputField<Int?>,
    val calculatedGrade: GradeWithPercentage?,
    val gradeScoreThresholds: List<GradeScoreThreshold>,
)

internal data class GradeScoreThreshold(val grade: BigDecimal, val minThreshold: Int)

internal data class GradeWithPercentage(val grade: BigDecimal, val percentage: Int)