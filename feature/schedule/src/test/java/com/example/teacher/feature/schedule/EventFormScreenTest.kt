package com.example.teacher.feature.schedule

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.data.repository.event.EventRepository
import com.example.teacher.core.database.datasource.event.EventDataSource
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.feature.schedule.data.EventFormViewModel
import com.example.teacher.feature.schedule.nav.ScheduleNavigation
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import java.time.DayOfWeek
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class EventFormScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var eventDataSource: EventDataSource

    @Inject
    lateinit var eventRepository: EventRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun formExists() {
        val eventLessonTitleMatcher =
            hasNoClickAction() and hasText(rule.activity.getString(R.string.schedule_class_date))
        val viewModel = ViewModelProvider(rule.activity)[EventFormViewModel::class.java]
        rule.setContent { EventFormScreen(viewModel) }
        rule.onRoot().printToLog("Event Form")

        rule.onNode(eventLessonTitleMatcher).assertExists()
    }

    @Test
    fun eventTypeChange() {
        val eventLessonButtonMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.schedule_class_date))
        val eventButtonMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.schedule_event))
        val eventLessonTitleMatcher =
            hasNoClickAction() and hasText(rule.activity.getString(R.string.schedule_class_date))
        val eventTitleMatcher =
            hasNoClickAction() and hasText(rule.activity.getString(R.string.schedule_event_date))
        val viewModel = ViewModelProvider(rule.activity)[EventFormViewModel::class.java]
        rule.setContent { EventFormScreen(viewModel) }
        rule.onRoot().printToLog("Event Form")

        rule.onNode(eventLessonButtonMatcher).assertIsSelected()
        rule.onNode(eventLessonTitleMatcher).assertExists()
        rule.onNode(eventTitleMatcher).assertDoesNotExist()

        rule.onNode(eventButtonMatcher).performClick().assertIsSelected()

        rule.onNode(eventLessonTitleMatcher).assertDoesNotExist()
        rule.onNode(eventTitleMatcher).assertExists()
    }

    @Test
    fun lessonEventInputWorks() {
        val lessonResult =
            mutableStateOf(Result.Success(LessonPreviewParameterProvider().values.first()))
        val secondTermMatcher = hasText(
            rule.activity.getString(
                R.string.schedule_term_with_data,
                lessonResult.value.data.schoolClass.schoolYear.secondTerm.name
            )
        )
        val dayPickerMatcher = { dayOfWeek: DayOfWeek ->
            hasText(
                TimeUtils.getDisplayNameOfDayOfWeek(dayOfWeek).capitalize(Locale.current)
            ) and hasClickAction()
        }
        val okMatcher = hasText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_ok))
        val cancelMatcher =
            hasText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_cancel))
        val saveButtonMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.schedule_add_event_date))
        val savedStateHandle = SavedStateHandle(mapOf(ScheduleNavigation.lessonIdArg to 1L))
        val viewModel = EventFormViewModel(eventRepository, savedStateHandle)
        rule.setContent { EventFormScreen(viewModel, lessonResult) }
        rule.onRoot().printToLog("Event Form")

        rule.onNode(secondTermMatcher).performClick()
        rule.onNode(dayPickerMatcher(viewModel.form.value.day)).performClick()
        rule.onNode(dayPickerMatcher(DayOfWeek.FRIDAY)).performClick()
        rule.onNode(okMatcher).performClick()
        rule.onNodeWithText("08:00").performClick()
        rule.onNode(okMatcher).performClick()
        rule.onNodeWithText("08:45").performClick()
        rule.onNode(okMatcher).performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.schedule_once)).performClick()
        rule.onNodeWithText(TimeUtils.format(viewModel.form.value.date)).performClick()
        rule.onNode(cancelMatcher).performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.schedule_weekly)).performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.schedule_is_cancelled)).performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.schedule_is_cancelled)).performClick()
        rule.onNode(saveButtonMatcher)
            .assertIsEnabled()
            .performClick()
    }

    @Test
    fun eventUpdateWorks() = runTest {
        givenEvent(eventDataSource)
        val saveButtonMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.schedule_add_event_date))
        val savedStateHandle = SavedStateHandle(mapOf(ScheduleNavigation.eventIdArg to 1L))
        val viewModel = EventFormViewModel(eventRepository, savedStateHandle)
        rule.setContent { EventFormScreen(viewModel) }

        rule.waitUntil { viewModel.eventResult.value is Result.Success }
        rule.onRoot().printToLog("Event Form")

        rule.onNode(saveButtonMatcher)
            .assertIsEnabled()
            .performClick()
    }

    @Test
    fun eventInputWorks() {
        val eventButtonMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.schedule_event))
        val eventNameInputMatcher =
            hasText(rule.activity.getString(R.string.schedule_event_name_label), substring = true)
        val saveButtonMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.schedule_add_event_date))
        val viewModel = ViewModelProvider(rule.activity)[EventFormViewModel::class.java]
        rule.setContent { EventFormScreen(viewModel) }
        rule.onRoot().printToLog("Event Form")

        rule.onNode(eventButtonMatcher).performClick().assertIsSelected()
        rule.onNode(eventNameInputMatcher).performTextInput("Event #1")
        rule.onNode(saveButtonMatcher).assertIsEnabled().performClick()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun EventFormScreen(
        viewModel: EventFormViewModel,
        lessonResult: State<Result<Lesson?>> = viewModel.lessonResult.collectAsStateWithLifecycle(),
    ) {
        val isLessonForm by viewModel.isLessonForm.collectAsStateWithLifecycle()
        val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
        val form by viewModel.form.collectAsStateWithLifecycle()
        val isEditMode = false
        val showDayPicker = remember(isEditMode, isLessonForm, form.type) {
            val isDayPickerFormType =
                (form.type == EventType.Weekly || form.type == EventType.EveryTwoWeeks)
            !isEditMode && isLessonForm && isDayPickerFormType
        }
        val showTypeControls = remember(isEditMode, isLessonForm) { !isEditMode && isLessonForm }
        EventFormScreen(
            lessonResult = lessonResult.value,
            snackbarHostState = remember { SnackbarHostState() },
            showNavigationIcon = false,
            onNavBack = {},
            onDeleteClick = {},
            onLessonPickerClick = {},
            eventForm = form,
            isLessonForm = isLessonForm,
            showTermPicker = showDayPicker,
            showDayPicker = showDayPicker,
            showTypeControls = showTypeControls,
            onIsLessonFormChange = viewModel::onIsLessonFormChange,
            onNameChange = viewModel::onNameChange,
            onDayChange = viewModel::onDayChange,
            onDateChange = viewModel::onDateChange,
            onStartTimeChange = viewModel::onStartTimeChange,
            onEndTimeChange = viewModel::onEndTimeChange,
            onTermSelected = viewModel::onTermSelected,
            onTypeChange = viewModel::onTypeChange,
            onIsCancelledChange = viewModel::onIsCancelledChange,
            isSubmitEnabled = form.isSubmitEnabled,
            onSubmit = viewModel::onSubmit,
            isEditMode = isEditMode,
            isDeleted = isDeleted,
        )
    }
}