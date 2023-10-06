package com.example.teacher.feature.note.nav

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
import com.example.teacher.feature.note.NoteFormScreen
import com.example.teacher.feature.note.R
import com.example.teacher.feature.note.data.NoteFormViewModel

@Composable
internal fun NoteFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    isEditMode: Boolean,
    viewModel: NoteFormViewModel = hiltViewModel(),
) {
    val noteResult by viewModel.noteResult.collectAsStateWithLifecycle()
    val isNoteDeleted by viewModel.isNoteDeleted.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()
    val isFormMutated by viewModel.isFormMutated.collectAsStateWithLifecycle()
    val status = form.status

    // Observe save.
    LaunchedEffect(status) {
        if (status == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.note_note_saved)
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isNoteDeleted) {
        if (isNoteDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.note_note_deleted)
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
        noteResult = noteResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = { backPressedDispatcher?.onBackPressed() },
        onDeleteNoteClick = { showDeleteDialog = true },
        formStatus = form.status,
        title = form.title,
        onTitleChange = viewModel::onTitleChange,
        text = form.text,
        onTextChange = viewModel::onTextChange,
        priority = form.priority,
        onPriorityChange = viewModel::onPriorityChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddNote = viewModel::onSubmit,
        isEditMode = isEditMode,
        isNoteDeleted = isNoteDeleted,
    )
}