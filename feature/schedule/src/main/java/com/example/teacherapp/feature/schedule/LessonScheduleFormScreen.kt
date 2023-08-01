package com.example.teacherapp.feature.schedule

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
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherRadioButton
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.component.picker.TeacherTimePicker
import com.example.teacherapp.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.schedule.data.LessonScheduleForm
import com.example.teacherapp.feature.schedule.data.LessonScheduleFormProvider
import com.example.teacherapp.feature.schedule.data.LessonScheduleFormType
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonScheduleFormScreen(
    lessonResult: Result<Lesson>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    lessonScheduleForm: LessonScheduleForm,
    onDateChange: (date: LocalDate) -> Unit,
    onStartTimeChange: (date: LocalTime) -> Unit,
    onEndTimeChange: (date: LocalTime) -> Unit,
    onTypeChange: (type: LessonScheduleFormType) -> Unit,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TeacherTopBar(
                title = "Dodaj termin zajęć",
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
            Header(lessonResult = lessonResult)

            DateForm(
                date = lessonScheduleForm.date,
                onDateChange = onDateChange,
                startTime = lessonScheduleForm.startTime,
                onStartTimeChange = onStartTimeChange,
                endTime = lessonScheduleForm.endTime,
                onEndTimeChange = onEndTimeChange,
                type = lessonScheduleForm.type,
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
    lessonResult: Result<Lesson>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (lessonResult is Result.Success) {
            val lessonName = lessonResult.data.name
            val schoolClassName = lessonResult.data.schoolClass.name
            Text(
                text = "$lessonName $schoolClassName",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }
}

@Composable
private fun DateForm(
    date: LocalDate,
    onDateChange: (date: LocalDate) -> Unit,
    startTime: LocalTime,
    onStartTimeChange: (date: LocalTime) -> Unit,
    endTime: LocalTime,
    onEndTimeChange: (date: LocalTime) -> Unit,
    type: LessonScheduleFormType,
    onTypeChange: (type: LessonScheduleFormType) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small),
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Termin zajęć",
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

            Column(Modifier.selectableGroup()) {
                TeacherRadioButton(
                    label = "Jednorazowe",
                    selected = type == LessonScheduleFormType.Once,
                    onClick = { onTypeChange(LessonScheduleFormType.Once) },
                )
                TeacherRadioButton(
                    label = "Cotygodniowe",
                    selected = type == LessonScheduleFormType.Weekly,
                    onClick = { onTypeChange(LessonScheduleFormType.Weekly) },
                )
                TeacherRadioButton(
                    label = "Co 2 tygodnie",
                    selected = type == LessonScheduleFormType.EveryTwoWeeks,
                    onClick = { onTypeChange(LessonScheduleFormType.EveryTwoWeeks) },
                )
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
private fun LessonScheduleFormScreenPreview(
    @PreviewParameter(LessonPreviewParameterProvider::class, limit = 1) lesson: Lesson,
) {
    TeacherAppTheme {
        Surface {
            var form by remember { mutableStateOf(LessonScheduleFormProvider.createDefaultForm()) }

            LessonScheduleFormScreen(
                lessonResult = Result.Success(lesson),
                showNavigationIcon = true,
                onNavBack = {},
                lessonScheduleForm = form,
                onDateChange = { form = form.copy(date = it) },
                onStartTimeChange = { form = form.copy(startTime = it) },
                onEndTimeChange = { form = form.copy(endTime = it) },
                onTypeChange = { form = form.copy(type = it) },
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = {},
            )
        }
    }
}