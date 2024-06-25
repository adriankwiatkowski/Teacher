package com.example.teacher.feature.schedule

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.datasource.event.EventDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.schedule.data.ScheduleViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import java.time.LocalDate
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScheduleScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var eventDataSource: EventDataSource

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
        val viewModel = ViewModelProvider(rule.activity)[ScheduleViewModel::class.java]
        rule.setContent { ScheduleScreen(viewModel) }
        rule.onRoot().printToLog("Schedule")

        rule.waitUntil { viewModel.eventsResult.value is Result.Success }

        rule.onNodeWithText(rule.activity.getString(R.string.schedule_schedule_empty))
            .assertExists()
    }

    @Test
    fun list() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenEvent(eventDataSource, lessonId = 1L)
        givenEvent(eventDataSource, lessonId = 1L, isCancelled = true)
        givenEvent(eventDataSource, name = "Event #1")
        givenEvent(eventDataSource, isCancelled = true)
        val viewModel = ViewModelProvider(rule.activity)[ScheduleViewModel::class.java]
        rule.setContent { ScheduleScreen(viewModel) }
        rule.onRoot().printToLog("Schedule")

        rule.waitUntil { viewModel.eventsResult.value is Result.Success }

        rule.onNodeWithText("Event #1").assertExists()
    }

    @Test
    fun dateControlWorks() {
        val prevDateMatcher =
            hasContentDescription(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_navigate_before))
        val nextDateMatcher =
            hasContentDescription(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_navigate_next))
        val toString = { date: LocalDate -> TimeUtils.format(date) }
        val viewModel = ViewModelProvider(rule.activity)[ScheduleViewModel::class.java]
        rule.setContent { ScheduleScreen(viewModel) }
        rule.onRoot().printToLog("Schedule")

        var date = TimeUtils.currentDate()
        viewModel.onDateSelected(date)

        rule.onNodeWithText(toString(date)).assertExists()

        rule.onNode(prevDateMatcher).performClick()
        date = TimeUtils.minusDays(date, 1)
        rule.onNodeWithText(toString(date)).assertExists()

        rule.onNode(nextDateMatcher).performClick()
        date = TimeUtils.plusDays(date, 1)
        rule.onNodeWithText(toString(date)).assertExists()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun ScheduleScreen(viewModel: ScheduleViewModel) {
        val eventsResult by viewModel.eventsResult.collectAsStateWithLifecycle()
        val date by viewModel.date.collectAsStateWithLifecycle()
        ScheduleScreen(
            eventsResult = eventsResult,
            snackbarHostState = remember { SnackbarHostState() },
            date = date,
            onDateSelected = viewModel::onDateSelected,
            onPrevDateClick = viewModel::onPrevDateClick,
            onNextDateClick = viewModel::onNextDateClick,
            onScheduleClick = {},
            onAddScheduleClick = {},
        )
    }
}