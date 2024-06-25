package com.example.teacher.feature.lesson.attendance

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.data.repository.lessonattendance.LessonAttendanceRepository
import com.example.teacher.core.database.datasource.event.EventDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.lessonattendance.LessonAttendanceDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.attendance.data.AttendancesViewModel
import com.example.teacher.feature.lesson.givenEvent
import com.example.teacher.feature.lesson.givenLesson
import com.example.teacher.feature.lesson.givenLessonAttendance
import com.example.teacher.feature.lesson.givenSchoolClass
import com.example.teacher.feature.lesson.givenStudent
import com.example.teacher.feature.lesson.nav.LessonNavigation
import com.example.teacher.feature.lesson.testSchoolClass
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AttendancesScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var lessonAttendanceDataSource: LessonAttendanceDataSource

    @Inject
    lateinit var eventDataSource: EventDataSource

    @Inject
    lateinit var lessonDataSource: LessonDataSource

    @Inject
    lateinit var studentDataSource: StudentDataSource

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Inject
    lateinit var lessonAttendanceRepository: LessonAttendanceRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun happyPath() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenEvent(eventDataSource, lessonId = 1L)
        givenEvent(
            eventDataSource,
            lessonId = 1L,
            date = TimeUtils.plusDays(TimeUtils.currentDate(), 14),
        )
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 1L, studentId = 1L)
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 2L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.lessonIdArg to 1L))
        val lesson = lessonDataSource.getLessonById(1L).first()!!
        val viewModel = AttendancesViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val attendancesUiStateResult by viewModel.attendancesUiStateResult.collectAsStateWithLifecycle()
            val studentsWithAttendanceResult by viewModel.studentsWithAttendanceResult.collectAsStateWithLifecycle()

            AttendancesScreen(
                attendancesUiStateResult = attendancesUiStateResult,
                studentsWithAttendanceResult = studentsWithAttendanceResult,
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                onScheduleAttendanceClick = {},
                onAddScheduleClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendances")
        rule.waitUntil { viewModel.attendancesUiStateResult.value is Result.Success }
    }

    @Test
    fun empty() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.lessonIdArg to 1L))
        val lesson = lessonDataSource.getLessonById(1L).first()!!
        val viewModel = AttendancesViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val attendancesUiStateResult by viewModel.attendancesUiStateResult.collectAsStateWithLifecycle()
            val studentsWithAttendanceResult by viewModel.studentsWithAttendanceResult.collectAsStateWithLifecycle()

            AttendancesScreen(
                attendancesUiStateResult = attendancesUiStateResult,
                studentsWithAttendanceResult = studentsWithAttendanceResult,
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                onScheduleAttendanceClick = {},
                onAddScheduleClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendances")
        rule.waitUntil { viewModel.attendancesUiStateResult.value is Result.Success }

        rule.onNodeWithText(rule.activity.getString(R.string.lesson_attendance_no_schedule))
            .assertExists()
    }

    @Test
    fun statisticsShown() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenEvent(eventDataSource, lessonId = 1L)
        givenEvent(
            eventDataSource,
            lessonId = 1L,
            date = TimeUtils.plusDays(TimeUtils.currentDate(), 14),
        )
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 1L, studentId = 1L)
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 2L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.lessonIdArg to 1L))
        val lesson = lessonDataSource.getLessonById(1L).first()!!
        val viewModel = AttendancesViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val attendancesUiStateResult by viewModel.attendancesUiStateResult.collectAsStateWithLifecycle()
            val studentsWithAttendanceResult by viewModel.studentsWithAttendanceResult.collectAsStateWithLifecycle()

            AttendancesScreen(
                attendancesUiStateResult = attendancesUiStateResult,
                studentsWithAttendanceResult = studentsWithAttendanceResult,
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                onScheduleAttendanceClick = {},
                onAddScheduleClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendances")
        rule.waitUntil { viewModel.attendancesUiStateResult.value is Result.Success }

        rule.onNodeWithText(rule.activity.getString(R.string.lesson_attendance_statistics))
            .performClick()

        rule.onNodeWithText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_ok))
            .assertExists()
            .assertHasClickAction()
    }
}