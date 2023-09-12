package com.example.teacher.feature.schedule

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherSwitch
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.result.DeletedScreen
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schedule.component.DateForm
import com.example.teacher.feature.schedule.data.EventForm
import com.example.teacher.feature.schedule.data.EventFormProvider
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventFormScreen(
    lessonResult: Result<Lesson?>,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onDeleteClick: () -> Unit,
    onLessonPickerClick: () -> Unit,
    eventForm: EventForm,
    isLessonForm: Boolean,
    showTermPicker: Boolean,
    showDayPicker: Boolean,
    showTypeControls: Boolean,
    onIsLessonFormChange: (isLessonFormChange: Boolean) -> Unit,
    onDayChange: (day: DayOfWeek) -> Unit,
    onDateChange: (date: LocalDate) -> Unit,
    onStartTimeChange: (date: LocalTime) -> Unit,
    onEndTimeChange: (date: LocalTime) -> Unit,
    onTermSelected: (isFirstTermSelected: Boolean) -> Unit,
    onTypeChange: (type: EventType) -> Unit,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    isEditMode: Boolean,
    isDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = if (isEditMode) {
                    stringResource(R.string.schedule_event_form_edit_title)
                } else {
                    stringResource(R.string.schedule_event_form_title)
                },
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(TeacherActions.delete(onDeleteClick))
                } else {
                    emptyList()
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        if (isDeleted) {
            DeletedScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(MaterialTheme.spacing.medium),
                label = stringResource(R.string.schedule_event_deleted),
            )
        } else {
            MainContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(MaterialTheme.spacing.medium),
                lessonResult = lessonResult,
                onLessonPickerClick = onLessonPickerClick,
                eventForm = eventForm,
                isLessonForm = isLessonForm,
                showTermPicker = showTermPicker,
                showDayPicker = showDayPicker,
                showTypeControls = showTypeControls,
                onIsLessonFormChange = onIsLessonFormChange,
                onDayChange = onDayChange,
                onDateChange = onDateChange,
                onStartTimeChange = onStartTimeChange,
                onEndTimeChange = onEndTimeChange,
                onTermSelected = onTermSelected,
                onTypeChange = onTypeChange,
                isSubmitEnabled = isSubmitEnabled,
                onSubmit = onSubmit,
            )
        }
    }
}

@Composable
private fun MainContent(
    lessonResult: Result<Lesson?>,
    onLessonPickerClick: () -> Unit,
    eventForm: EventForm,
    isLessonForm: Boolean,
    showTermPicker: Boolean,
    showDayPicker: Boolean,
    showTypeControls: Boolean,
    onIsLessonFormChange: (isLessonFormChange: Boolean) -> Unit,
    onDayChange: (day: DayOfWeek) -> Unit,
    onDateChange: (date: LocalDate) -> Unit,
    onStartTimeChange: (date: LocalTime) -> Unit,
    onEndTimeChange: (date: LocalTime) -> Unit,
    onTermSelected: (isFirstTermSelected: Boolean) -> Unit,
    onTypeChange: (type: EventType) -> Unit,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val lesson = remember(lessonResult) {
            if (lessonResult is Result.Success) {
                lessonResult.data
            } else {
                null
            }
        }

        Header(
            lesson = lesson,
            onLessonPickerClick = onLessonPickerClick,
            isLessonForm = isLessonForm,
            onIsLessonFormChange = onIsLessonFormChange,
        )

        val schoolYear = lesson?.schoolClass?.schoolYear
        DateForm(
            title = if (lesson != null) {
                stringResource(R.string.schedule_class_date)
            } else {
                stringResource(R.string.schedule_event_date)
            },
            firstTermName = schoolYear?.firstTerm?.name
                ?: stringResource(R.string.schedule_first_term),
            secondTermName = schoolYear?.secondTerm?.name
                ?: stringResource(R.string.schedule_second_term),
            day = eventForm.day,
            onDayChange = onDayChange,
            date = eventForm.date,
            onDateChange = onDateChange,
            startTime = eventForm.startTime,
            onStartTimeChange = onStartTimeChange,
            endTime = eventForm.endTime,
            onEndTimeChange = onEndTimeChange,
            showTermPicker = showTermPicker,
            showDayPicker = showDayPicker,
            showTypeControls = showTypeControls,
            isFirstTermSelected = eventForm.isFirstTermSelected,
            onTermSelected = onTermSelected,
            type = eventForm.type,
            onTypeChange = onTypeChange,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.schedule_add_event_date),
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        )
    }
}

@Composable
private fun Header(
    lesson: Lesson?,
    onLessonPickerClick: () -> Unit,
    isLessonForm: Boolean,
    onIsLessonFormChange: (isLessonFormChange: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.animateContentSize()) {
        val text = if (lesson != null) {
            val lessonName = lesson.name
            val schoolClassName = lesson.schoolClass.name
            "$lessonName $schoolClassName"
        } else {
            stringResource(R.string.schedule_pick_lesson)
        }

        TeacherSwitch(
            label = stringResource(R.string.schedule_add_event),
            checked = !isLessonForm,
            onCheckedChange = { onIsLessonFormChange(!it) },
        )

        if (isLessonForm) {
            Text(
                text = stringResource(R.string.schedule_lesson),
                style = MaterialTheme.typography.labelMedium,
            )
            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                label = text,
                onClick = onLessonPickerClick,
            )
            if (lesson == null) {
                Text(
                    text = stringResource(R.string.schedule_lesson_not_selected),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }
}

@Preview
@Composable
private fun EventFormScreenPreview(
    @PreviewParameter(
        LessonPreviewParameterProvider::class,
        limit = 1,
    ) lesson: Lesson,
) {
    TeacherTheme {
        Surface {
            var form by remember { mutableStateOf(EventFormProvider.createDefaultForm()) }
            var isLessonForm by remember { mutableStateOf(true) }
            val showDayPicker = isLessonForm && form.type in setOf(
                EventType.Weekly,
                EventType.EveryTwoWeeks,
            )

            EventFormScreen(
                lessonResult = Result.Success(lesson),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onDeleteClick = {},
                onLessonPickerClick = {},
                eventForm = form,
                isLessonForm = isLessonForm,
                showTermPicker = showDayPicker,
                showDayPicker = showDayPicker,
                showTypeControls = isLessonForm,
                onIsLessonFormChange = { isLessonForm = it },
                onDayChange = {
                    form = form.copy(day = EventFormProvider.sanitizeDay(it))
                },
                onDateChange = {
                    form = form.copy(date = EventFormProvider.sanitizeDate(it))
                },
                onStartTimeChange = {
                    val timeData = EventFormProvider.sanitizeStartTime(it, form.endTime)
                    form = form.copy(
                        startTime = timeData.startTime,
                        endTime = timeData.endTime,
                    )
                },
                onEndTimeChange = {
                    val timeData = EventFormProvider.sanitizeEndTime(form.startTime, it)
                    form = form.copy(
                        startTime = timeData.startTime,
                        endTime = timeData.endTime,
                    )
                },
                onTermSelected = { form = form.copy(isFirstTermSelected = it) },
                onTypeChange = { form = form.copy(type = it) },
                isSubmitEnabled = form.isSubmitEnabled,
                isEditMode = true,
                isDeleted = false,
                onSubmit = {},
            )
        }
    }
}