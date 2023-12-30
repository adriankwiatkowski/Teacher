package com.example.teacher.feature.grade

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.grade.data.GradeFormProvider
import com.example.teacher.feature.grade.data.GradeFormViewModel
import com.example.teacher.feature.grade.data.GradeScoreDataProvider
import com.example.teacher.feature.grade.paramprovider.GradeFormUiStatePreviewParameterProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import java.math.BigDecimal

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GradeFormScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun gradeErrorWhenEmpty() {
        val viewModel = ViewModelProvider(rule.activity)[GradeFormViewModel::class.java]
        val uiState = viewModel.uiState

        rule.setContent {
            val uiStateResult by uiState.collectAsStateWithLifecycle()
            val form by viewModel.form.collectAsStateWithLifecycle()
            val initialGrade by viewModel.initialGrade.collectAsStateWithLifecycle()
            val gradeScoreData by viewModel.gradeScoreData.collectAsStateWithLifecycle()
            val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
            GradeFormScreen(
                uiStateResult = uiStateResult,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = false,
                onNavBack = {},
                formStatus = form.status,
                initialGrade = initialGrade,
                inputGrade = form.grade.value,
                onGradeChange = viewModel::onGradeChange,
                gradeScoreData = gradeScoreData,
                onGradeScoreThresholdChange = viewModel::onGradeScoreThresholdChange,
                onMaxScoreChange = viewModel::onMaxScoreChange,
                onStudentScoreChange = viewModel::onStudentScoreChange,
                onSaveGradeScore = viewModel::onSaveGradeScore,
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = viewModel::onSubmit,
                isEditMode = false,
                isDeleted = isDeleted,
                onDeleteClick = {},
            )
        }

        viewModel.onGradeChange(DecimalUtils.Six)

        rule.onRoot().printToLog("Grade Form")
        rule.waitUntil { uiState.value is Result.Error }
    }

    @Test
    fun gradesClickable() {
        val uiState = GradeFormUiStatePreviewParameterProvider().values.first()
        var form by mutableStateOf(GradeFormProvider.createDefaultForm())
        var gradeScoreData by mutableStateOf(GradeScoreDataProvider.createDefault())

        rule.setContent {
            GradeFormScreen(
                uiStateResult = Result.Success(uiState),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                formStatus = form.status,
                initialGrade = null,
                inputGrade = form.grade.value,
                onGradeChange = { form = form.copy(grade = GradeFormProvider.validateGrade(it)) },
                gradeScoreData = gradeScoreData,
                onGradeScoreThresholdChange = { grade, minThreshold ->
                    gradeScoreData = GradeScoreDataProvider.validateGradeScores(
                        gradeScoreData = gradeScoreData,
                        grade = grade,
                        newMinThreshold = minThreshold,
                    )
                },
                onMaxScoreChange = { maxScore ->
                    gradeScoreData = gradeScoreData.copy(
                        maxScore = GradeScoreDataProvider.validateGradeScore(maxScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onStudentScoreChange = { studentScore ->
                    gradeScoreData = gradeScoreData.copy(
                        studentScore = GradeScoreDataProvider.validateGradeScore(studentScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onSaveGradeScore = {},
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = {},
                isEditMode = false,
                isDeleted = false,
                onDeleteClick = {},
            )
        }

        for (grade in grades) {
            rule.onNode(hasText(DecimalUtils.toGrade(grade)) and hasClickAction())
                .assertExists()
                .performClick()
            val newGrade = rule.activity.getString(
                R.string.grade_new_grade,
                toGradeWithLiteral(form.grade.value),
            )
            rule.onNode(hasText(newGrade) and hasNoClickAction())
                .assertExists()
        }
    }

    @Test
    fun gradeScoreVisible() {
        val uiState = GradeFormUiStatePreviewParameterProvider().values.first()
        var form by mutableStateOf(GradeFormProvider.createDefaultForm())
        var gradeScoreData by mutableStateOf(GradeScoreDataProvider.createDefault())

        rule.setContent {
            GradeFormScreen(
                uiStateResult = Result.Success(uiState),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                formStatus = form.status,
                initialGrade = null,
                inputGrade = form.grade.value,
                onGradeChange = { form = form.copy(grade = GradeFormProvider.validateGrade(it)) },
                gradeScoreData = gradeScoreData,
                onGradeScoreThresholdChange = { grade, minThreshold ->
                    gradeScoreData = GradeScoreDataProvider.validateGradeScores(
                        gradeScoreData = gradeScoreData,
                        grade = grade,
                        newMinThreshold = minThreshold,
                    )
                },
                onMaxScoreChange = { maxScore ->
                    gradeScoreData = gradeScoreData.copy(
                        maxScore = GradeScoreDataProvider.validateGradeScore(maxScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onStudentScoreChange = { studentScore ->
                    gradeScoreData = gradeScoreData.copy(
                        studentScore = GradeScoreDataProvider.validateGradeScore(studentScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onSaveGradeScore = {},
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = {},
                isEditMode = false,
                isDeleted = false,
                onDeleteClick = {},
            )
        }

        rule.onNodeWithText(rule.activity.getString(R.string.grade_score_input)).performClick()

        rule.onNodeWithText(rule.activity.getString(R.string.grade_score_thresholds_save_info))
            .assertExists()
        rule.onAllNodesWithText(rule.activity.getString(R.string.grade_score_thresholds_save))
            .assertCountEquals(2)
            .assertAll(hasClickAction())
            .onFirst()
            .performClick()
    }

    private fun toGradeWithLiteral(grade: BigDecimal?): String {
        return if (grade != null) {
            rule.activity.getString(
                R.string.grade_grade_with_literal,
                DecimalUtils.toGrade(grade),
                DecimalUtils.toLiteral(grade),
            )
        } else {
            rule.activity.getString(R.string.grade_no_grade)
        }
    }

    private val grades = listOf(
        DecimalUtils.One,
        DecimalUtils.TwoMinus,
        DecimalUtils.Two,
        DecimalUtils.TwoPlus,
        DecimalUtils.ThreeMinus,
        DecimalUtils.Three,
        DecimalUtils.ThreePlus,
        DecimalUtils.FourMinus,
        DecimalUtils.Four,
        DecimalUtils.FourPlus,
        DecimalUtils.FiveMinus,
        DecimalUtils.Five,
        DecimalUtils.FivePlus,
        DecimalUtils.SixMinus,
        DecimalUtils.Six,
    )
}