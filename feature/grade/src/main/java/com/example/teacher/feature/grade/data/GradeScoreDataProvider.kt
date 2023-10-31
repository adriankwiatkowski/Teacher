package com.example.teacher.feature.grade.data

import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.common.utils.DecimalUtils.safeDivide
import com.example.teacher.core.model.data.GradeScore
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.feature.grade.R
import java.math.BigDecimal

internal object GradeScoreDataProvider {

    fun validateGradeScore(gradeScore: String?, isEdited: Boolean = true): InputField<Int?> {
        val gradeScoreInt = gradeScore?.toIntOrNull()
        val isError = gradeScore == null || gradeScoreInt == null || gradeScoreInt < 0
        val supportingText = if (isError) R.string.grades_grade_score_input_error else null
        return InputField(
            gradeScoreInt,
            supportingText = supportingText,
            isError = isError,
            isEdited = isEdited,
        )
    }

    fun validateGradeScores(
        gradeScoreData: GradeScoreData,
        grade: BigDecimal,
        newMinThreshold: Int,
    ): GradeScoreData {
        val gradeScores = gradeScoreData.gradeScoreThresholds
        val changedIndex = gradeScores.indexOfFirst { it.grade == grade }
        val changedScore = gradeScores[changedIndex]

        if (changedScore.minThreshold == newMinThreshold) {
            return gradeScoreData
        }

        @Suppress("UnnecessaryVariable") val minValidNewMinThreshold = changedIndex
        val maxValidNewMinThreshold = 100 - gradeScores.size + 1 + changedIndex
        @Suppress("NAME_SHADOWING") val newMinThreshold =
            newMinThreshold.coerceIn(minValidNewMinThreshold, maxValidNewMinThreshold)

        val newGradeScores = gradeScores.toMutableList()
        newGradeScores[changedIndex] = changedScore.copy(minThreshold = newMinThreshold)

        if (newMinThreshold > changedScore.minThreshold) {
            for (i in (changedIndex + 1)..<newGradeScores.size) {
                val prevScore = newGradeScores[i - 1]
                val currScore = newGradeScores[i]
                if (currScore.minThreshold > prevScore.minThreshold) {
                    break
                }

                newGradeScores[i] = currScore.copy(minThreshold = prevScore.minThreshold + 1)
            }
        } else {
            for (i in (changedIndex - 1) downTo 0) {
                val nextScore = newGradeScores[i + 1]
                val currScore = newGradeScores[i]
                if (currScore.minThreshold < nextScore.minThreshold) {
                    break
                }

                newGradeScores[i] = currScore.copy(minThreshold = nextScore.minThreshold - 1)
            }
        }

        @Suppress("NAME_SHADOWING") val gradeScoreData =
            gradeScoreData.copy(gradeScoreThresholds = newGradeScores)
        return calculateGrade(gradeScoreData)
    }

    fun calculateGrade(gradeScoreData: GradeScoreData): GradeScoreData {
        return gradeScoreData.copy(calculatedGrade = getGrade(gradeScoreData))
    }

    fun createDefault(
        maxScore: String? = "100",
        studentScore: String? = "80"
    ): GradeScoreData {
        return GradeScoreData(
            maxScore = validateGradeScore(maxScore, isEdited = false),
            studentScore = validateGradeScore(studentScore, isEdited = false),
            calculatedGrade = null,
            gradeScoreThresholds = emptyList(),
        ).let { gradeScoreData -> calculateGrade(gradeScoreData) }
    }

    fun getGradeScoreThresholds(gradeScore: GradeScore?): List<GradeScoreThreshold> {
        return if (gradeScore == null) {
            emptyList()
        } else {
            listOf(
                GradeScoreThreshold(
                    DecimalUtils.TwoMinus,
                    gradeScore.twoMinusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.Two,
                    gradeScore.twoMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.TwoPlus,
                    gradeScore.twoPlusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.ThreeMinus,
                    gradeScore.threeMinusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.Three,
                    gradeScore.threeMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.ThreePlus,
                    gradeScore.threePlusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.FourMinus,
                    gradeScore.fourMinusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.Four,
                    gradeScore.fourMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.FourPlus,
                    gradeScore.fourPlusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.FiveMinus,
                    gradeScore.fiveMinusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.Five,
                    gradeScore.fiveMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.FivePlus,
                    gradeScore.fivePlusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.SixMinus,
                    gradeScore.sixMinusMinThreshold.toInt(),
                ),
                GradeScoreThreshold(
                    DecimalUtils.Six,
                    gradeScore.sixMinThreshold.toInt(),
                ),
            )
        }
    }

    private fun getGrade(gradeScoreData: GradeScoreData): GradeWithPercentage? {
        val maxScore = gradeScoreData.maxScore
        val studentScore = gradeScoreData.studentScore
        if (maxScore.isError || maxScore.value == null) {
            return null
        }
        if (studentScore.isError || studentScore.value == null) {
            return null
        }

        val percentage = BigDecimal(studentScore.value!!)
            .safeDivide(BigDecimal(maxScore.value!!))
            .times(BigDecimal(100))
            .toInt()

        for (gradeScoreThreshold in gradeScoreData.gradeScoreThresholds.asReversed()) {
            if (percentage >= gradeScoreThreshold.minThreshold) {
                return GradeWithPercentage(
                    grade = gradeScoreThreshold.grade,
                    percentage = percentage,
                )
            }
        }

        return GradeWithPercentage(grade = DecimalUtils.One, percentage = percentage)
    }
}