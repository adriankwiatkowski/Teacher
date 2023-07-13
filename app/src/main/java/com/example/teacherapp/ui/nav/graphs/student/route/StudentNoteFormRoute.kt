package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.screens.student.note.StudentNoteFormScreen
import com.example.teacherapp.ui.screens.student.note.data.StudentNoteFormViewModel

@Composable
internal fun StudentNoteFormRoute(
    onNavBack: () -> Unit,
    setTitle: (String) -> Unit,
    showSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentNoteFormViewModel = hiltViewModel(),
) {
    val studentNoteResource by viewModel.studentNoteResource.collectAsStateWithLifecycle()
    val studentFullName by viewModel.studentFullName.collectAsStateWithLifecycle()
    val isStudentDeleted = viewModel.isStudentNoteDeleted
    val form = viewModel.form

    // Set title.
    LaunchedEffect(Unit) {
        setTitle("Uwaga")
    }
    // Observe deletion.
    LaunchedEffect(isStudentDeleted) {
        if (isStudentDeleted) {
            showSnackbar("Usunięto uwagę")
            onNavBack()
        }
    }
    // Add/remove action menu.
    DisposableEffect(studentNoteResource, viewModel, viewModel::onDeleteStudentNote) {
        val resource = studentNoteResource as? Resource.Success
        resource?.data ?: return@DisposableEffect onDispose {}

        val menuItems =
            listOf(ActionMenuItemProvider.delete(onClick = viewModel::onDeleteStudentNote))

        addActionMenuItems(menuItems)

        onDispose {
            removeActionMenuItems(menuItems)
        }
    }

    StudentNoteFormScreen(
        modifier = modifier,
        studentNoteResource = studentNoteResource,
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