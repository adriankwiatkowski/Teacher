package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.student.note.StudentNoteFormScreen
import com.example.teacherapp.ui.screens.student.note.data.StudentNoteFormViewModel

@Composable
internal fun StudentNoteFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    viewModel: StudentNoteFormViewModel = hiltViewModel(),
) {
    val studentNoteResult by viewModel.studentNoteResult.collectAsStateWithLifecycle()
    val studentFullName by viewModel.studentFullName.collectAsStateWithLifecycle()
    val isStudentDeleted = viewModel.isStudentNoteDeleted
    val form = viewModel.form

    // Observe deletion.
    LaunchedEffect(isStudentDeleted) {
        if (isStudentDeleted) {
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
        isValid = form.isValid,
        onAddStudentNote = viewModel::onSubmit,
        onStudentNoteAdded = onNavBack,
        isStudentNoteDeleted = viewModel.isStudentNoteDeleted,
    )
}