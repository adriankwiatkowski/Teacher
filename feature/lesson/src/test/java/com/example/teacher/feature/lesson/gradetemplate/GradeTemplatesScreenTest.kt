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
import com.example.teacher.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.givenGradeTemplate
import com.example.teacher.feature.lesson.givenLesson
import com.example.teacher.feature.lesson.givenSchoolClass
import com.example.teacher.feature.lesson.gradetemplate.data.GradeTemplatesViewModel
import com.example.teacher.feature.lesson.paramprovider.GradeTemplatesUiStatePreviewParameterProvider
import com.example.teacher.feature.lesson.testSchoolClass
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
class GradeTemplatesScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var gradeTemplateDataSource: GradeTemplateDataSource

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
    fun empty() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenGradeTemplate(gradeTemplateDataSource, lessonId = 1L)
        val lesson = LessonPreviewParameterProvider().values.first()
        val viewModel = ViewModelProvider(rule.activity)[GradeTemplatesViewModel::class.java]

        rule.setContent {
            val gradeTemplateUiStateResult by viewModel.gradeTemplateUiStateResult.collectAsStateWithLifecycle()

            GradeTemplatesScreen(
                gradeTemplateUiStateResult = gradeTemplateUiStateResult,
                snackbarHostState = remember { SnackbarHostState() },
                lesson = lesson,
                onGradeClick = {},
                onAddGradeClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Activity")
        rule.waitUntil { viewModel.gradeTemplateUiStateResult.value is Result.Success }
    }

    @Test
    fun data() {
        val gradeTemplateUiState = GradeTemplatesUiStatePreviewParameterProvider().values.first()
        val lesson = LessonPreviewParameterProvider().values.first()
        rule.setContent {
            GradeTemplatesScreen(
                gradeTemplateUiStateResult = Result.Success(gradeTemplateUiState),
                snackbarHostState = remember { SnackbarHostState() },
                lesson = lesson,
                onGradeClick = {},
                onAddGradeClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Activity")
        rule.onNodeWithText(
            rule.activity.getString(
                R.string.lesson_term,
                lesson.schoolClass.schoolYear.firstTerm.name,
            )
        ).assertExists()
        rule.onNodeWithText(
            rule.activity.getString(
                R.string.lesson_term,
                lesson.schoolClass.schoolYear.secondTerm.name,
            )
        ).assertExists()
    }
}