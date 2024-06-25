package com.example.teacher.feature.studentnote.nav

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
import com.example.teacher.feature.studentnote.R
import com.example.teacher.feature.studentnote.StudentNoteFormScreen
import com.example.teacher.feature.studentnote.data.StudentNoteFormViewModel

@Composable
internal fun StudentNoteFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    isEditMode: Boolean,
    viewModel: StudentNoteFormViewModel = hiltViewModel(),
) {
    val studentNoteResult by viewModel.studentNoteResult.collectAsStateWithLifecycle()
    val studentFullName by viewModel.studentFullName.collectAsStateWithLifecycle()
    val isStudentNoteDeleted by viewModel.isStudentNoteDeleted.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()
    val isFormMutated by viewModel.isFormMutated.collectAsStateWithLifecycle()
    val status = form.status

    // Observe save.
    LaunchedEffect(status) {
        if (status == FormStatus.Success) {
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isStudentNoteDeleted) {
        if (isStudentNoteDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.student_note_note_deleted)
            onNavBack()
        }
    }

    // Handle delete dialog confirmation.
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    if (showDeleteDialog) {
        TeacherDeleteDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmClick = {
                viewModel.onDeleteStudentNote()
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

    StudentNoteFormScreen(
        studentNoteResult = studentNoteResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = { backPressedDispatcher?.onBackPressed() },
        onDeleteStudentNoteClick = { showDeleteDialog = true },
        formStatus = form.status,
        studentFullName = studentFullName.orEmpty(),
        title = form.title,
        onTitleChange = viewModel::onTitleChange,
        description = form.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        isNoteNegative = form.isNegative,
        onIsNoteNegativeChange = viewModel::onIsNoteNegativeChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddStudentNote = viewModel::onSubmit,
        isEditMode = isEditMode,
        isStudentNoteDeleted = isStudentNoteDeleted,
    )
}