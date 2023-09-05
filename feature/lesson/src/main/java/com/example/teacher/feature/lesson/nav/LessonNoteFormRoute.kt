package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.model.FormStatus
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
    val form = viewModel.form
    val formStatus = form.status

    // Observe save.
    LaunchedEffect(formStatus, onShowSnackbar, onNavBack) {
        if (formStatus == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.lesson_lesson_note_saved)
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isDeleted, onShowSnackbar, onNavBack) {
        if (isDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.lesson_lesson_note_deleted)
            onNavBack()
        }
    }

    NoteFormScreen(
        noteResult = lessonNoteResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        onDeleteNoteClick = viewModel::onDeleteNote,
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