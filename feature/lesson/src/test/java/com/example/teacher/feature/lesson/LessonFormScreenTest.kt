package com.example.teacher.feature.lesson

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.lesson.data.LessonFormViewModel
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
class LessonFormScreenTest {

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
    fun loaded() {
        val viewModel = ViewModelProvider(rule.activity)[LessonFormViewModel::class.java]

        rule.setContent {
            val result by viewModel.lessonResult.collectAsStateWithLifecycle()
            val form by viewModel.form.collectAsStateWithLifecycle()
            LessonFormScreen(
                lessonResult = result,
                snackbarHostState = remember { SnackbarHostState() },
                formStatus = form.status,
                name = form.name,
                onNameChange = viewModel::onNameChange,
                isSubmitEnabled = form.isSubmitEnabled,
                schoolClassName = "1 A",
                onAddLessonClick = {},
                showNavigationIcon = true,
                onNavBack = {},
            )
        }

        rule.waitUntil { viewModel.lessonResult.value is Result.Success }

        rule.onRoot().printToLog("Lesson Form")
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_add_lesson)).assertExists()
    }

    @Test
    fun inputWorks() {
        val viewModel = ViewModelProvider(rule.activity)[LessonFormViewModel::class.java]

        rule.setContent {
            val result by viewModel.lessonResult.collectAsStateWithLifecycle()
            val form by viewModel.form.collectAsStateWithLifecycle()
            LessonFormScreen(
                lessonResult = result,
                snackbarHostState = remember { SnackbarHostState() },
                formStatus = form.status,
                name = form.name,
                onNameChange = viewModel::onNameChange,
                isSubmitEnabled = form.isSubmitEnabled,
                schoolClassName = "1 A",
                onAddLessonClick = {},
                showNavigationIcon = true,
                onNavBack = {},
            )
        }

        rule.waitUntil { viewModel.lessonResult.value is Result.Success }

        rule.onRoot().printToLog("Lesson Form")
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_add_lesson))
            .assertHasClickAction()
            .assertIsDisplayed()
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.lesson_lesson))
            .performTextInput("Matematyka")
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_add_lesson))
            .assertHasClickAction()
            .assertIsEnabled()
    }
}