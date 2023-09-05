package com.example.teacher.feature.note.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.model.FormStatus
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
    val form = viewModel.form

    // Observe save.
    LaunchedEffect(form.status) {
        if (form.status == FormStatus.Success) {
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

    NoteFormScreen(
        noteResult = noteResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        onDeleteNoteClick = viewModel::onDeleteNote,
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