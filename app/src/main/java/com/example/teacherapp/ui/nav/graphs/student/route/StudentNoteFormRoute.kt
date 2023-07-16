package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.screens.student.note.StudentNoteFormScreen
import com.example.teacherapp.ui.screens.student.note.data.StudentNoteFormViewModel

@Composable
internal fun StudentNoteFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    isEditMode: Boolean,
    viewModel: StudentNoteFormViewModel = hiltViewModel(),
) {
    val studentNoteResult by viewModel.studentNoteResult.collectAsStateWithLifecycle()
    val studentFullName by viewModel.studentFullName.collectAsStateWithLifecycle()
    val isStudentNoteDeleted by viewModel.isStudentNoteDeleted.collectAsStateWithLifecycle()
    val form = viewModel.form

    // Observe save.
    LaunchedEffect(form.status, onNavBack) {
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