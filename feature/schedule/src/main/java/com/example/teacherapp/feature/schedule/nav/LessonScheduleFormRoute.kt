package com.example.teacherapp.feature.schedule.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.feature.schedule.data.LessonCalendarForm
import com.example.teacherapp.feature.schedule.data.LessonCalendarFormType
import com.example.teacherapp.feature.schedule.LessonScheduleFormScreen

@Composable
internal fun LessonScheduleFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
) {
    // TODO: Get data from ViewModel.
    var calendarForm by remember {
        mutableStateOf(
            LessonCalendarForm(
                date = TimeUtils.currentDate(),
                startTime = TimeUtils.localTimeOf(8, 0),
                endTime = TimeUtils.localTimeOf(8, 45),
                type = LessonCalendarFormType.Weekly,
            )
        )
    }

    LessonScheduleFormScreen(
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        schoolClassName = "1A",
        lessonName = "Matematyka",
        lessonCalendarForm = calendarForm,
        onDateChange = { calendarForm = calendarForm.copy(date = it) },
        onStartTimeChange = { calendarForm = calendarForm.copy(startTime = it) },
        onEndTimeChange = { calendarForm = calendarForm.copy(endTime = it) },
        onTypeChange = { calendarForm = calendarForm.copy(type = it) },
    )
}