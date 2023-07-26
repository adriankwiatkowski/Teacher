package com.example.teacherapp.feature.note.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.note.NoteFormScreen
import com.example.teacherapp.feature.note.data.NoteFormViewModel

@Composable
internal fun NoteFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    isEditMode: Boolean,
    viewModel: NoteFormViewModel = hiltViewModel(),
) {
    val noteResult by viewModel.noteResult.collectAsStateWithLifecycle()
    val isNoteDeleted by viewModel.isNoteDeleted.collectAsStateWithLifecycle()
    val form = viewModel.form

    // Observe save.
    LaunchedEffect(form.status, onShowSnackbar, onNavBack) {
        if (form.status == FormStatus.Success) {
            onShowSnackbar("Zapisano notatkę")
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isNoteDeleted, onShowSnackbar, onNavBack) {
        if (isNoteDeleted) {
            onShowSnackbar("Usunięto notatkę")
            onNavBack()
        }
    }

    NoteFormScreen(
        noteResult = noteResult,
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