package com.example.teacher.feature.grade.data

import com.example.teacher.core.model.data.GradeScore
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

internal fun GradeScoreData.toGradeScore(id: Long, gradeTemplateId: Long): GradeScore {
    return GradeScore(
        id = id,
        gradeTemplateId = gradeTemplateId,
        twoMinusMinThreshold = this.gradeScoreThresholds[0].minThreshold.toLong(),
        twoMinThreshold = this.gradeScoreThresholds[1].minThreshold.toLong(),
        twoPlusMinThreshold = this.gradeScoreThresholds[2].minThreshold.toLong(),
        threeMinusMinThreshold = this.gradeScoreThresholds[3].minThreshold.toLong(),
        threeMinThreshold = this.gradeScoreThresholds[4].minThreshold.toLong(),
        threePlusMinThreshold = this.gradeScoreThresholds[5].minThreshold.toLong(),
        fourMinusMinThreshold = this.gradeScoreThresholds[6].minThreshold.toLong(),
        fourMinThreshold = this.gradeScoreThresholds[7].minThreshold.toLong(),
        fourPlusMinThreshold = this.gradeScoreThresholds[8].minThreshold.toLong(),
        fiveMinusMinThreshold = this.gradeScoreThresholds[9].minThreshold.toLong(),
        fiveMinThreshold = this.gradeScoreThresholds[10].minThreshold.toLong(),
        fivePlusMinThreshold = this.gradeScoreThresholds[11].minThreshold.toLong(),
        sixMinusMinThreshold = this.gradeScoreThresholds[12].minThreshold.toLong(),
        sixMinThreshold = this.gradeScoreThresholds[13].minThreshold.toLong(),
    )
}