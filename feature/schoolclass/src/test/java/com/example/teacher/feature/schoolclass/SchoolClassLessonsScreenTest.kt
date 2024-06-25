package com.example.teacher.feature.schoolclass

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.BasicLessonsPreviewParameterProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SchoolClassLessonsScreenTest {

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
        rule.setContent { SchoolClassLessonsScreen(emptyList()) }
        rule.onRoot().printToLog("School Class Lessons")

        rule.onNodeWithText(rule.activity.getString(R.string.school_class_no_lessons_in_class))
            .assertExists()
    }

    @Test
    fun labelExists() {
        val lessons = BasicLessonsPreviewParameterProvider().values.first()
        rule.setContent { SchoolClassLessonsScreen(lessons) }
        rule.onRoot().printToLog("School Class Lessons")

        rule.onNodeWithText(lessons.first().name).assertExists()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun SchoolClassLessonsScreen(lessons: List<BasicLesson>) {
        SchoolClassLessonsScreen(
            snackbarHostState = remember { SnackbarHostState() },
            lessons = lessons,
            onLessonClick = {},
            onAddLessonClick = {},
        )
    }
}