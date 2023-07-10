package com.example.teacherapp.ui.nav.graphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.screens.studentnote.StudentNoteFormScreen
import com.example.teacherapp.ui.screens.studentnote.data.StudentNoteFormViewModel

fun NavGraphBuilder.addStudentNoteRouteGraph(
    navController: NavController,
    navActions: TeacherNavigationActions,
    setTitle: (String) -> Unit,
    showSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
) {
    composable(
        TeacherDestinations.STUDENT_NOTE_FORM_ROUTE,
        arguments = listOf(
            navArgument(TeacherDestinationsArgs.STUDENT_ID_ARG) {
                type = NavType.LongType
            },
            navArgument(TeacherDestinationsArgs.STUDENT_NOTE_ID_ARG) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) {
        val viewModel: StudentNoteFormViewModel = hiltViewModel()
        val studentNoteResource by viewModel.studentNoteResource.collectAsStateWithLifecycle()
        val studentFullName by viewModel.studentFullName.collectAsStateWithLifecycle()
        val isStudentDeleted = viewModel.isStudentDeleted
        val form = viewModel.form

        // Set title.
        LaunchedEffect(Unit) {
            setTitle("Uwaga")
        }
        // Observe deletion.
        LaunchedEffect(isStudentDeleted) {
            if (isStudentDeleted) {
                showSnackbar("Usunięto uwagę")
                navController.navigateUp()
            }
        }
        // Add/remove action menu.
        DisposableEffect(studentNoteResource, viewModel, viewModel::onDeleteStudentNote) {
            val resource = studentNoteResource as? Resource.Success
            resource?.data ?: return@DisposableEffect onDispose {}

            val menuItems = listOf(
                ActionMenuItem(
                    name = "",
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    onClick = viewModel::onDeleteStudentNote,
                ),
            )

            addActionMenuItems(menuItems)

            onDispose {
                removeActionMenuItems(menuItems)
            }
        }

        StudentNoteFormScreen(
            studentNoteResource = studentNoteResource,
            formStatus = form.status,
            studentFullName = studentFullName.orEmpty(),
            title = form.title,
            onTitleChange = viewModel::onTitleChange,
            description = form.description,
            onDescriptionChange = viewModel::onDescriptionChange,
            isValid = form.isValid,
            onAddStudentNote = viewModel::onSubmit,
            onStudentNoteAdded = { navController.navigateUp() },
            isStudentNoteDeleted = viewModel.isStudentDeleted,
        )
    }
}