package com.example.teacher.feature.schedule

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.schedule.data.LessonPickerViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LessonPickerScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var lessonDataSource: LessonDataSource

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun empty() {
        val viewModel = ViewModelProvider(rule.activity)[LessonPickerViewModel::class.java]
        rule.setContent { LessonPickerScreen(viewModel) }
        rule.onRoot().printToLog("Lesson Picker")

        rule.onNodeWithText(rule.activity.getString(R.string.schedule_no_lesson_exists))
            .assertExists()
    }

    @Test
    fun list() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass(name = "1A"))
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenLesson(lessonDataSource, schoolClassId = 1L, name = "Mathematics")
        givenLesson(lessonDataSource, schoolClassId = 1L, name = "Physics")
        givenLesson(lessonDataSource, schoolClassId = 2L, name = "Physics")
        givenLesson(lessonDataSource, schoolClassId = 3L, name = "Physics")
        val viewModel = ViewModelProvider(rule.activity)[LessonPickerViewModel::class.java]
        rule.setContent { LessonPickerScreen(viewModel) }

        rule.waitUntil { viewModel.lessonsByYearResult.value is Result.Success }
        rule.onRoot().printToLog("Lesson Picker")

        rule.onNodeWithText("Mathematics 1A").assertExists().performClick()
        rule.onAllNodesWithText("Physics", substring = true).assertCountEquals(3)
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun LessonPickerScreen(viewModel: LessonPickerViewModel) {
        val lessonsByYearResult by viewModel.lessonsByYearResult.collectAsStateWithLifecycle()
        LessonPickerScreen(
            lessonsByYearResult = lessonsByYearResult,
            snackbarHostState = remember { SnackbarHostState() },
            showNavigationIcon = false,
            onNavBack = {},
            onLessonClick = {},
        )
    }
}