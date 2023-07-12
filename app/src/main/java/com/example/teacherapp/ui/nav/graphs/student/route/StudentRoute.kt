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
import com.example.teacherapp.ui.screens.student.StudentScreen
import com.example.teacherapp.ui.screens.student.data.StudentViewModel

@Composable
internal fun StudentRoute(
    onEditClick: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    onNavBack: () -> Unit,
    setTitle: (String) -> Unit,
    showSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentViewModel = hiltViewModel(),
) {
    val studentResource by viewModel.studentResource.collectAsStateWithLifecycle()
    val studentNotesResource by viewModel.studentNotesResource.collectAsStateWithLifecycle()
    val isStudentDeleted = viewModel.isStudentDeleted

    // Set title.
    LaunchedEffect(studentResource) {
        val student = studentResource as? Resource.Success
        val schoolName = student?.data?.schoolClass?.name.orEmpty()
        setTitle("Klasa $schoolName")
    }
    // Observe deletion.
    LaunchedEffect(isStudentDeleted) {
        if (isStudentDeleted) {
            showSnackbar("Usunięto ucznia")
            onNavBack()
        }
    }
    // Add/remove action menu.
    DisposableEffect(Unit) {
        val menuItems = listOf(
            ActionMenuItemProvider.edit(onClick = onEditClick),
            ActionMenuItemProvider.delete(onClick = viewModel::deleteStudent),
        )

        addActionMenuItems(menuItems)

        onDispose {
            removeActionMenuItems(menuItems)
        }
    }

    StudentScreen(
        modifier = modifier,
        studentResource = studentResource,
        studentNotesResource = studentNotesResource,
        onEmailClick = {},
        onPhoneClick = {},
        onGradeClick = {},
        onAddGradeClick = {},
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
        isGradesExpanded = viewModel.isGradesExpanded,
        isNotesExpanded = viewModel.isNotesExpanded,
        isStudentDeleted = viewModel.isStudentDeleted,
    )
}