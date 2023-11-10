package com.example.teacher.feature.schedule.nav

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.ui.component.TeacherDeleteDialog
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.BackPressDiscardDialogHandler
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.schedule.EventFormScreen
import com.example.teacher.feature.schedule.R
import com.example.teacher.feature.schedule.data.EventFormViewModel

@Composable
internal fun EventFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onSave: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    onLessonPickerClick: () -> Unit,
    isEditMode: Boolean,
    viewModel: EventFormViewModel = hiltViewModel(),
) {
    val lessonResult by viewModel.lessonResult.collectAsStateWithLifecycle()
    val isLessonForm by viewModel.isLessonForm.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()
    val isFormMutated by viewModel.isFormMutated.collectAsStateWithLifecycle()
    val status = form.status

    // Observe save.
    LaunchedEffect(status) {
        if (status == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.schedule_event_saved)
            onSave()
        }
    }
    // Observe deletion.
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.schedule_event_deleted)
            onNavBack()
        }
    }

    // Handle delete dialog confirmation.
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    if (showDeleteDialog) {
        TeacherDeleteDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmClick = {
                viewModel.onDelete()
                showDeleteDialog = false
            },
        )
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    BackPressDiscardDialogHandler(
        enabled = isFormMutated,
        backPressedDispatcher = backPressedDispatcher,
        onDiscard = onNavBack,
    )

    val showDayPicker = remember(isEditMode, isLessonForm, form.type) {
        val isDayPickerFormType =
            (form.type == EventType.Weekly || form.type == EventType.EveryTwoWeeks)
        !isEditMode && isLessonForm && isDayPickerFormType
    }
    val showTypeControls = remember(isEditMode, isLessonForm) { !isEditMode && isLessonForm }

    EventFormScreen(
        lessonResult = lessonResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = { backPressedDispatcher?.onBackPressed() },
        onDeleteClick = { showDeleteDialog = true },
        onLessonPickerClick = onLessonPickerClick,
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