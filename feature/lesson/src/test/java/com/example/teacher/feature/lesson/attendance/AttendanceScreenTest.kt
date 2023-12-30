package com.example.teacher.feature.lesson.attendance

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.lessonattendance.LessonAttendanceRepository
import com.example.teacher.core.database.datasource.event.EventDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.lessonattendance.LessonAttendanceDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.attendance.data.AttendanceViewModel
import com.example.teacher.feature.lesson.givenEvent
import com.example.teacher.feature.lesson.givenLesson
import com.example.teacher.feature.lesson.givenLessonAttendance
import com.example.teacher.feature.lesson.givenSchoolClass
import com.example.teacher.feature.lesson.givenStudent
import com.example.teacher.feature.lesson.nav.LessonNavigation
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
class AttendanceScreenTest {

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
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 1L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.eventIdArg to 1L))
        val viewModel = AttendanceViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val eventResult by viewModel.eventResult.collectAsStateWithLifecycle()
            val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
            val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

            AttendanceScreen(
                eventResult = eventResult,
                snackbarHostState = remember { SnackbarHostState() },
                lessonAttendancesResult = lessonAttendancesResult,
                showNavigationIcon = false,
                onNavBack = {},
                onLessonAttendanceClick = viewModel::onLessonAttendanceClick,
                showAttendanceDialog = dialogState != null,
                selectedStudentFullName = dialogState?.studentFullName.orEmpty(),
                selectedAttendance = dialogState?.attendance,
                onAttendanceSelect = viewModel::onAttendanceSelect,
                onAttendanceDismissRequest = viewModel::onAttendanceDismissRequest,
                onAttendanceConfirmClick = viewModel::onAttendanceConfirmClick,
                onEventEditClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendance")
        rule.waitUntil { viewModel.lessonAttendancesResult.value is Result.Success }
    }

    @Test
    fun dialogShows() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenEvent(eventDataSource, lessonId = 1L, isCancelled = true)
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 1L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.eventIdArg to 1L))
        val viewModel = AttendanceViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val eventResult by viewModel.eventResult.collectAsStateWithLifecycle()
            val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
            val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

            AttendanceScreen(
                eventResult = eventResult,
                snackbarHostState = remember { SnackbarHostState() },
                lessonAttendancesResult = lessonAttendancesResult,
                showNavigationIcon = false,
                onNavBack = {},
                onLessonAttendanceClick = viewModel::onLessonAttendanceClick,
                showAttendanceDialog = dialogState != null,
                selectedStudentFullName = dialogState?.studentFullName.orEmpty(),
                selectedAttendance = dialogState?.attendance,
                onAttendanceSelect = viewModel::onAttendanceSelect,
                onAttendanceDismissRequest = viewModel::onAttendanceDismissRequest,
                onAttendanceConfirmClick = viewModel::onAttendanceConfirmClick,
                onEventEditClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendance")
        rule.waitUntil { viewModel.lessonAttendancesResult.value is Result.Success }

        rule.onNodeWithText("Name Surname").performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_set_attendance))
            .assertExists()
    }

    @Test
    fun dialogCancel() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenEvent(eventDataSource, lessonId = 1L)
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 1L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.eventIdArg to 1L))
        val viewModel = AttendanceViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val eventResult by viewModel.eventResult.collectAsStateWithLifecycle()
            val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
            val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

            AttendanceScreen(
                eventResult = eventResult,
                snackbarHostState = remember { SnackbarHostState() },
                lessonAttendancesResult = lessonAttendancesResult,
                showNavigationIcon = false,
                onNavBack = {},
                onLessonAttendanceClick = viewModel::onLessonAttendanceClick,
                showAttendanceDialog = dialogState != null,
                selectedStudentFullName = dialogState?.studentFullName.orEmpty(),
                selectedAttendance = dialogState?.attendance,
                onAttendanceSelect = viewModel::onAttendanceSelect,
                onAttendanceDismissRequest = viewModel::onAttendanceDismissRequest,
                onAttendanceConfirmClick = viewModel::onAttendanceConfirmClick,
                onEventEditClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendance")
        rule.waitUntil { viewModel.lessonAttendancesResult.value is Result.Success }

        rule.onNodeWithText("Name Surname").performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_set_attendance)).assertExists()

        rule.onNodeWithText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_cancel))
            .performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_set_attendance))
            .assertDoesNotExist()
    }

    @Test
    fun dialogChangeToLate() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenEvent(eventDataSource, lessonId = 1L)
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 1L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.eventIdArg to 1L))
        val viewModel = AttendanceViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val eventResult by viewModel.eventResult.collectAsStateWithLifecycle()
            val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
            val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

            AttendanceScreen(
                eventResult = eventResult,
                snackbarHostState = remember { SnackbarHostState() },
                lessonAttendancesResult = lessonAttendancesResult,
                showNavigationIcon = false,
                onNavBack = {},
                onLessonAttendanceClick = viewModel::onLessonAttendanceClick,
                showAttendanceDialog = dialogState != null,
                selectedStudentFullName = dialogState?.studentFullName.orEmpty(),
                selectedAttendance = dialogState?.attendance,
                onAttendanceSelect = viewModel::onAttendanceSelect,
                onAttendanceDismissRequest = viewModel::onAttendanceDismissRequest,
                onAttendanceConfirmClick = viewModel::onAttendanceConfirmClick,
                onEventEditClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendance")
        rule.waitUntil { viewModel.lessonAttendancesResult.value is Result.Success }

        rule.onNodeWithText("Name Surname").performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_set_attendance)).assertExists()

        rule.onNodeWithText(rule.activity.getString(R.string.lesson_late))
            .performClick()
        rule.onNodeWithText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_ok))
            .performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_set_attendance))
            .assertDoesNotExist()
    }

    @Test
    fun dialogChangeToUnset() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L)
        givenEvent(eventDataSource, lessonId = 1L)
        givenLessonAttendance(lessonAttendanceDataSource, eventId = 1L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(LessonNavigation.eventIdArg to 1L))
        val viewModel = AttendanceViewModel(lessonAttendanceRepository, savedStateHandle)

        rule.setContent {
            val eventResult by viewModel.eventResult.collectAsStateWithLifecycle()
            val lessonAttendancesResult by viewModel.lessonAttendancesResult.collectAsStateWithLifecycle()
            val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

            AttendanceScreen(
                eventResult = eventResult,
                snackbarHostState = remember { SnackbarHostState() },
                lessonAttendancesResult = lessonAttendancesResult,
                showNavigationIcon = false,
                onNavBack = {},
                onLessonAttendanceClick = viewModel::onLessonAttendanceClick,
                showAttendanceDialog = dialogState != null,
                selectedStudentFullName = dialogState?.studentFullName.orEmpty(),
                selectedAttendance = dialogState?.attendance,
                onAttendanceSelect = viewModel::onAttendanceSelect,
                onAttendanceDismissRequest = viewModel::onAttendanceDismissRequest,
                onAttendanceConfirmClick = viewModel::onAttendanceConfirmClick,
                onEventEditClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Attendance")
        rule.waitUntil { viewModel.lessonAttendancesResult.value is Result.Success }

        rule.onNodeWithText("Name Surname").performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_set_attendance)).assertExists()

        rule.onNodeWithText(rule.activity.getString(R.string.lesson_no_attendance))
            .performClick()
        rule.onNodeWithText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_ok))
            .performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_set_attendance))
            .assertDoesNotExist()
    }
}