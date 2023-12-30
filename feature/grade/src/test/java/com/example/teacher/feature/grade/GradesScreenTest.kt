package com.example.teacher.feature.grade

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.BasicGradesForTemplatePreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.GradeTemplateInfoPreviewParameterProvider
import com.example.teacher.feature.grade.data.GradesUiState
import com.example.teacher.feature.grade.data.GradesViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GradesScreenTest {

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
    fun gradesErrorWhenEmpty() {
        val viewModel = ViewModelProvider(rule.activity)[GradesViewModel::class.java]
        val uiState = viewModel.uiState

        rule.setContent {
            val uiStateResult by uiState.collectAsStateWithLifecycle()
            GradesScreen(
                uiStateResult = uiStateResult,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                isDeleted = false,
                onDeleteClick = {},
                onEditClick = {},
                onStudentClick = { _, _ -> },
            )
        }

        rule.onRoot().printToLog("Grades")
        rule.waitUntil { uiState.value is Result.Error }
    }

    @Test
    fun gradesLoading() {
        val grades = BasicGradesForTemplatePreviewParameterProvider().values.first()
        val uiState = GradesUiState(
            grades = grades,
            gradeTemplateInfo = GradeTemplateInfoPreviewParameterProvider().values.first(),
        )
        val gradeName = rule.activity.getString(
            R.string.grades_grade_with_term,
            uiState.gradeTemplateInfo.gradeName,
            if (uiState.gradeTemplateInfo.isFirstTerm) {
                uiState.gradeTemplateInfo.lesson.schoolClass.schoolYear.firstTerm.name
            } else {
                uiState.gradeTemplateInfo.lesson.schoolClass.schoolYear.secondTerm.name
            },
        )
        val gradeDescription = uiState.gradeTemplateInfo.gradeDescription

        rule.setContent {
            val uiStateResult by remember { mutableStateOf(Result.Success(uiState)) }
            GradesScreen(
                uiStateResult = uiStateResult,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                isDeleted = false,
                onDeleteClick = {},
                onEditClick = {},
                onStudentClick = { _, _ -> },
            )
        }

        rule.onRoot().printToLog("Grades")
        rule.onNodeWithText(gradeName).assertExists()
        gradeDescription?.let { desc -> rule.onNodeWithText(desc).assertExists() }
    }
}