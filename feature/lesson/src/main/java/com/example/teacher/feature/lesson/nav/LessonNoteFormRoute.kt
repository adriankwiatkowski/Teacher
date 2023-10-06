package com.example.teacher.feature.lesson.nav

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.component.TeacherDeleteDialog
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.BackPressDiscardDialogHandler
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.note.NoteFormScreen
import com.example.teacher.feature.lesson.note.data.NoteFormViewModel

@Composable
internal fun LessonNoteFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    isEditMode: Boolean,
    viewModel: NoteFormViewModel = hiltViewModel(),
) {
    val lessonNoteResult by viewModel.noteResult.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()
    val isFormMutated by viewModel.isFormMutated.collectAsStateWithLifecycle()
    val status = form.status

    // Observe save.
    LaunchedEffect(status) {
        if (status == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.lesson_lesson_note_saved)
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.lesson_lesson_note_deleted)
            onNavBack()
        }
    }

    // Handle delete dialog confirmation.
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    if (showDeleteDialog) {
        TeacherDeleteDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmClick = {
                viewModel.onDeleteNote()
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

    NoteFormScreen(
        noteResult = lessonNoteResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = { backPressedDispatcher?.onBackPressed() },
        onDeleteNoteClick = { showDeleteDialog = true },
        formStatus = form.status,
        title = form.title,
        onTitleChange = viewModel::onTitleChange,
        text = form.text,
        onTextChange = viewModel::onTextChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddNote = viewModel::onSubmit,
        isEditMode = isEditMode,
        isNoteDeleted = isDeleted,
    )
}