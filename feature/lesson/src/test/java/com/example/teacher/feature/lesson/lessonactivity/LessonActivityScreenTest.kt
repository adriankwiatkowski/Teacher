package com.example.teacher.feature.lesson.lessonactivity

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.feature.lesson.lessonactivity.data.LessonActivityViewModel
import com.example.teacher.feature.lesson.paramprovider.LessonActivityUiStatePreviewParameterProvider
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
class LessonActivityScreenTest {

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
    fun empty() {
        val lesson = LessonPreviewParameterProvider().values.first()
        val viewModel = ViewModelProvider(rule.activity)[LessonActivityViewModel::class.java]

        rule.setContent {
            val lessonActivityUiStateResult by viewModel.lessonActivityUiStateResult.collectAsStateWithLifecycle()

            LessonActivityScreen(
                lessonActivityUiStateResult = lessonActivityUiStateResult,
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                onIncreaseLessonActivity = viewModel::onIncreaseLessonActivity,
                onDecreaseLessonActivity = viewModel::onDecreaseLessonActivity,
            )
        }

        rule.onRoot().printToLog("Lesson Activity")
        rule.waitUntil { viewModel.lessonActivityUiStateResult.value is Result.Success }
    }

    @Test
    fun input() {
        val lesson = LessonPreviewParameterProvider().values.first()
        val viewModel = ViewModelProvider(rule.activity)[LessonActivityViewModel::class.java]
        val lessonActivityUiState by mutableStateOf(LessonActivityUiStatePreviewParameterProvider().values.first())

        rule.setContent {
            LessonActivityScreen(
                lessonActivityUiStateResult = Result.Success(lessonActivityUiState),
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                onIncreaseLessonActivity = viewModel::onIncreaseLessonActivity,
                onDecreaseLessonActivity = viewModel::onDecreaseLessonActivity,
            )
        }

        viewModel.onIncreaseLessonActivity(lessonActivityUiState.firstTermLessonActivities.first())
        viewModel.onDecreaseLessonActivity(lessonActivityUiState.firstTermLessonActivities.first())

        rule.onRoot().printToLog("Lesson Activity")
        rule.onAllNodesWithText(
            lessonActivityUiState.firstTermLessonActivities.first().student.fullName,
            substring = true,
        )
    }
}