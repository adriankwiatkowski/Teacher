package com.example.teacherapp.feature.schedule

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.EventType
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherRadioButton
import com.example.teacherapp.core.ui.component.TeacherSwitch
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.picker.TeacherTimePicker
import com.example.teacherapp.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.schedule.data.EventForm
import com.example.teacherapp.feature.schedule.data.EventFormProvider
import java.time.LocalDate
import java.time.LocalTime

// TODO: If is lesson form then allow to pick term, otherwise allow to specify title for event.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventFormScreen(
    lessonResult: Result<Lesson?>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onLessonPickerClick: () -> Unit,
    eventForm: EventForm,
    isLessonForm: Boolean,
    onIsLessonFormChange: (isLessonFormChange: Boolean) -> Unit,
    onDateChange: (date: LocalDate) -> Unit,
    onStartTimeChange: (date: LocalTime) -> Unit,
    onEndTimeChange: (date: LocalTime) -> Unit,
    onTypeChange: (type: EventType) -> Unit,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TeacherTopBar(
                title = "Dodaj termin",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(MaterialTheme.spacing.small),
        ) {
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

            DateForm(
                title = if (lesson != null) "Termin zajęć" else "Termin",
                date = eventForm.date,
                onDateChange = onDateChange,
                startTime = eventForm.startTime,
                onStartTimeChange = onStartTimeChange,
                endTime = eventForm.endTime,
                onEndTimeChange = onEndTimeChange,
                showTypeControls = isLessonForm,
                type = eventForm.type,
                onTypeChange = onTypeChange,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubmit,
                enabled = isSubmitEnabled,
            ) {
                Text(text = "Dodaj termin zajęć")
            }
        }
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
            "Wybierz przedmiot"
        }

        TeacherSwitch(
            label = "Dodaj wydarzenie",
            checked = !isLessonForm,
            onCheckedChange = { onIsLessonFormChange(!it) },
        )

        if (isLessonForm) {
            TeacherButton(modifier = Modifier.fillMaxWidth(), onClick = onLessonPickerClick) {
                Text(text = text)
            }
            if (lesson == null) {
                Text(
                    text = "Nie wybrano przedmiotu!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }
}

@Composable
private fun DateForm(
    title: String,
    date: LocalDate,
    onDateChange: (date: LocalDate) -> Unit,
    startTime: LocalTime,
    onStartTimeChange: (date: LocalTime) -> Unit,
    endTime: LocalTime,
    onEndTimeChange: (date: LocalTime) -> Unit,
    showTypeControls: Boolean,
    type: EventType,
    onTypeChange: (type: EventType) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small)
                .animateContentSize(),
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            LessonDatePicker(date = date, onDateSelected = onDateChange)
            LessonTimePicker(
                label = "Czas rozpoczęcia:",
                time = startTime,
                onTimeSelected = onStartTimeChange,
            )
            LessonTimePicker(
                label = "Czas zakończenia:",
                time = endTime,
                onTimeSelected = onEndTimeChange,
            )

            if (showTypeControls) {
                Column(Modifier.selectableGroup()) {
                    TeacherRadioButton(
                        label = "Jednorazowe",
                        selected = type == EventType.Once,
                        onClick = { onTypeChange(EventType.Once) },
                    )
                    TeacherRadioButton(
                        label = "Cotygodniowe",
                        selected = type == EventType.Weekly,
                        onClick = { onTypeChange(EventType.Weekly) },
                    )
                    TeacherRadioButton(
                        label = "Co 2 tygodnie",
                        selected = type == EventType.EveryTwoWeeks,
                        onClick = { onTypeChange(EventType.EveryTwoWeeks) },
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonDatePicker(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    Column {
        Text(
            text = TimeUtils.getDisplayNameOfDayOfWeek(date),
            style = MaterialTheme.typography.labelMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Data:")
            Text(text = TimeUtils.format(date))
            TeacherDatePicker(
                date = date,
                onDateSelected = onDateSelected,
                label = { Text("Wybierz datę") },
            )
        }
    }
}

@Composable
private fun LessonTimePicker(
    label: String,
    time: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)
        Text(text = TimeUtils.format(time))
        TeacherTimePicker(
            time = time,
            onTimeSelected = onTimeSelected,
            label = { Text("Wybierz godzinę") },
        )
    }
}

@Preview
@Composable
private fun EventFormScreenPreview(
    @PreviewParameter(LessonPreviewParameterProvider::class, limit = 1) lesson: Lesson,
) {
    TeacherAppTheme {
        Surface {
            var form by remember { mutableStateOf(EventFormProvider.createDefaultForm()) }
            var isLessonForm by remember { mutableStateOf(true) }

            EventFormScreen(
                lessonResult = Result.Success(lesson),
                showNavigationIcon = true,
                onNavBack = {},
                onLessonPickerClick = {},
                eventForm = form,
                isLessonForm = isLessonForm,
                onIsLessonFormChange = { isLessonForm = it },
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
                onTypeChange = { form = form.copy(type = it) },
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = {},
            )
        }
    }
}