package com.example.teacher.feature.lesson.gradetemplate

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
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
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.gradetemplate.data.GradeTemplateFormViewModel
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
class GradeTemplateFormScreenTest {

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
    fun inputWorks() {
        val viewModel = ViewModelProvider(rule.activity)[GradeTemplateFormViewModel::class.java]

        rule.setContent {
            val gradeTemplateResult by viewModel.gradeTemplateResult.collectAsStateWithLifecycle()
            val lessonResult by viewModel.lessonResult.collectAsStateWithLifecycle()
            val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
            val form by viewModel.form.collectAsStateWithLifecycle()
            val lesson = remember(lessonResult) { (lessonResult as? Result.Success)?.data }
            GradeTemplateFormScreen(
                gradeTemplateResult = gradeTemplateResult,
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = false,
                onNavBack = {},
                formStatus = form.status,
                name = form.name,
                onNameChange = viewModel::onNameChange,
                description = form.description,
                onDescriptionChange = viewModel::onDescriptionChange,
                weight = form.weight,
                onWeightChange = viewModel::onWeightChange,
                isFirstTerm = form.isFirstTerm,
                onIsFirstTermChange = viewModel::onIsFirstTermChange,
                isSubmitEnabled = form.isSubmitEnabled,
                onAddGrade = viewModel::onSubmit,
                isEditMode = false,
                isDeleted = isDeleted,
                onDeleteClick = {},
            )
        }

        rule.onRoot().printToLog("Grade Template Form")

        viewModel.onNameChange("Name")
        viewModel.onDescriptionChange("Description")
        viewModel.onWeightChange("5")
        viewModel.onIsFirstTermChange(true)

        rule.waitUntil { viewModel.gradeTemplateResult.value is Result.Success }
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_name), substring = true)
            .assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_description), substring = true)
            .assertExists()

        viewModel.onSubmit()
    }
}