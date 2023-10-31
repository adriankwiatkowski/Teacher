package com.example.teacher.core.database.datasource.gradescore

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.constant.GradeScoreConstant
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.querymapper.toExternal
import com.example.teacher.core.model.data.GradeScore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class GradeScoreDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GradeScoreDataSource {

    private val queries = db.gradeScoreQueries

    override fun getGradeScoreByGradeTemplateId(gradeTemplateId: Long): Flow<GradeScore?> =
        queries.transactionWithResult {
            val exists = queries.exists(gradeTemplateId).executeAsOneOrNull() != null

            if (!exists) {
                CoroutineScope(dispatcher).launch {
                    insertDefaultGradeScore(gradeTemplateId)
                }
            }

            queries
                .getGradeScoreByGradeTemplateId(gradeTemplateId)
                .asFlow()
                .mapToOne(dispatcher)
                .map(::toExternal)
                .flowOn(dispatcher)
        }

    override suspend fun updateGradeScore(
        gradeScore: GradeScore
    ): Unit = withContext(dispatcher) {
        queries.updateGradeScore(
            id = gradeScore.id,
            two_minus_min_threshold = gradeScore.twoMinusMinThreshold,
            two_min_threshold = gradeScore.twoMinThreshold,
            two_plus_min_threshold = gradeScore.twoPlusMinThreshold,
            three_minus_min_threshold = gradeScore.threeMinusMinThreshold,
            three_min_threshold = gradeScore.threeMinThreshold,
            three_plus_min_threshold = gradeScore.threePlusMinThreshold,
            four_minus_min_threshold = gradeScore.fourMinusMinThreshold,
            four_min_threshold = gradeScore.fourMinThreshold,
            four_plus_min_threshold = gradeScore.fourPlusMinThreshold,
            five_minus_min_threshold = gradeScore.fiveMinusMinThreshold,
            five_min_threshold = gradeScore.fiveMinThreshold,
            five_plus_min_threshold = gradeScore.fivePlusMinThreshold,
            six_minus_min_threshold = gradeScore.sixMinusMinThreshold,
            six_min_threshold = gradeScore.sixMinThreshold,
        )
    }

    private suspend fun insertDefaultGradeScore(
        gradeTemplateId: Long
    ): Unit = withContext(dispatcher) {
        queries.insertGradeScore(
            id = null,
            grade_template_id = gradeTemplateId,
            two_minus_min_threshold = GradeScoreConstant.DefaultTwoMinusMinThreshold,
            two_min_threshold = GradeScoreConstant.DefaultTwoMinThreshold,
            two_plus_min_threshold = GradeScoreConstant.DefaultTwoPlusMinThreshold,
            three_minus_min_threshold = GradeScoreConstant.DefaultThreeMinusMinThreshold,
            three_min_threshold = GradeScoreConstant.DefaultThreeMinThreshold,
            three_plus_min_threshold = GradeScoreConstant.DefaultThreePlusMinThreshold,
            four_minus_min_threshold = GradeScoreConstant.DefaultFourMinusMinThreshold,
            four_min_threshold = GradeScoreConstant.DefaultFourMinThreshold,
            four_plus_min_threshold = GradeScoreConstant.DefaultFourPlusMinThreshold,
            five_minus_min_threshold = GradeScoreConstant.DefaultFiveMinusMinThreshold,
            five_min_threshold = GradeScoreConstant.DefaultFiveMinThreshold,
            five_plus_min_threshold = GradeScoreConstant.DefaultFivePlusMinThreshold,
            six_minus_min_threshold = GradeScoreConstant.DefaultSixMinusMinThreshold,
            six_min_threshold = GradeScoreConstant.DefaultSixMinThreshold,
        )
    }
}