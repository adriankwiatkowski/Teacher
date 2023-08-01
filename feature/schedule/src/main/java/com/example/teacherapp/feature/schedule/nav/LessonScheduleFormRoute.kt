package com.example.teacherapp.feature.schedule.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.schedule.LessonScheduleFormScreen
import com.example.teacherapp.feature.schedule.data.LessonScheduleFormViewModel

@Composable
internal fun LessonScheduleFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onSave: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    viewModel: LessonScheduleFormViewModel = hiltViewModel(),
) {
    val lessonResult by viewModel.lessonResult.collectAsStateWithLifecycle()
    val form = viewModel.form

    // Observe save.
    LaunchedEffect(form.status, onShowSnackbar, onSave) {
        if (form.status == FormStatus.Success) {
            onShowSnackbar("Zapisano termin zajęć")
            onSave()
        }
    }

    LessonScheduleFormScreen(
        lessonResult = lessonResult,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        lessonScheduleForm = form,
        onDateChange = viewModel::onDateChange,
        onStartTimeChange = viewModel::onStartTimeChange,
        onEndTimeChange = viewModel::onEndTimeChange,
        onTypeChange = viewModel::onTypeChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onSubmit = viewModel::onSubmit,
    )
}