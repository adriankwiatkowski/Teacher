package com.example.teacherapp.feature.studentnote.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.studentnote.StudentNoteFormScreen
import com.example.teacherapp.feature.studentnote.data.StudentNoteFormViewModel

@Composable
internal fun StudentNoteFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: (message: String) -> Unit,
    isEditMode: Boolean,
    viewModel: StudentNoteFormViewModel = hiltViewModel(),
) {
    val studentNoteResult by viewModel.studentNoteResult.collectAsStateWithLifecycle()
    val studentFullName by viewModel.studentFullName.collectAsStateWithLifecycle()
    val isStudentNoteDeleted by viewModel.isStudentNoteDeleted.collectAsStateWithLifecycle()
    val form = viewModel.form

    // Observe save.
    LaunchedEffect(form.status, onShowSnackbar, onNavBack) {
        if (form.status == FormStatus.Success) {
            onShowSnackbar("Zapisano uwagę")
            onNavBack()
        }
    }
    // Observe deletion.
    LaunchedEffect(isStudentNoteDeleted, onShowSnackbar, onNavBack) {
        if (isStudentNoteDeleted) {
            onShowSnackbar("Usunięto uwagę")
            onNavBack()
        }
    }

    StudentNoteFormScreen(
        studentNoteResult = studentNoteResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        onDeleteStudentNoteClick = viewModel::onDeleteStudentNote,
        formStatus = form.status,
        studentFullName = studentFullName.orEmpty(),
        title = form.title,
        onTitleChange = viewModel::onTitleChange,
        description = form.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddStudentNote = viewModel::onSubmit,
        isEditMode = isEditMode,
        isStudentNoteDeleted = isStudentNoteDeleted,
    )
}