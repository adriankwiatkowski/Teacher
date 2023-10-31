package com.example.teacher.core.database.querymapper

import com.example.teacher.core.database.generated.queries.gradescore.GetGradeScoreByGradeTemplateId
import com.example.teacher.core.model.data.GradeScore

internal fun toExternal(gradeScore: GetGradeScoreByGradeTemplateId): GradeScore = GradeScore(
    id = gradeScore.id,
    gradeTemplateId = gradeScore.grade_template_id,
    twoMinusMinThreshold = gradeScore.two_minus_min_threshold,
    twoMinThreshold = gradeScore.two_min_threshold,
    twoPlusMinThreshold = gradeScore.two_plus_min_threshold,
    threeMinusMinThreshold = gradeScore.three_minus_min_threshold,
    threeMinThreshold = gradeScore.three_min_threshold,
    threePlusMinThreshold = gradeScore.three_plus_min_threshold,
    fourMinusMinThreshold = gradeScore.four_minus_min_threshold,
    fourMinThreshold = gradeScore.four_min_threshold,
    fourPlusMinThreshold = gradeScore.four_plus_min_threshold,
    fiveMinusMinThreshold = gradeScore.five_minus_min_threshold,
    fiveMinThreshold = gradeScore.five_min_threshold,
    fivePlusMinThreshold = gradeScore.five_plus_min_threshold,
    sixMinusMinThreshold = gradeScore.six_minus_min_threshold,
    sixMinThreshold = gradeScore.six_min_threshold,
)