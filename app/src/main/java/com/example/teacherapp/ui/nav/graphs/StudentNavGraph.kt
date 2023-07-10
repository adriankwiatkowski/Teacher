package com.example.teacherapp.ui.nav.graphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.teacherapp.ui.screens.student.StudentCreatorScreen
import com.example.teacherapp.ui.screens.student.StudentScreen
import com.example.teacherapp.ui.screens.student.data.StudentCreatorViewModel
import com.example.teacherapp.ui.screens.student.data.StudentViewModel

fun NavGraphBuilder.addStudentRouteGraph(
    navController: NavController,
    navActions: TeacherNavigationActions,
    setTitle: (String) -> Unit,
    showSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
) {
    composable(
        TeacherDestinations.STUDENT_ROUTE,
        arguments = listOf(
            navArgument(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG) {
                type = NavType.LongType
            },
            navArgument(TeacherDestinationsArgs.STUDENT_ID_ARG) {
                type = NavType.LongType
            },
        ),
    ) { backStackEntry ->
        val viewModel = hiltViewModel<StudentViewModel>()
        val studentResource by viewModel.uiState.collectAsStateWithLifecycle()

        val args = backStackEntry.arguments!!
        val schoolClassId = args.getLong(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG)
        val studentId = args.getLong(TeacherDestinationsArgs.STUDENT_ID_ARG)

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
                navController.navigateUp()
            }
        }
        // Add/remove action menu.
        DisposableEffect(Unit) {
            val menuItems = listOf(
                ActionMenuItem(
                    name = "",
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    onClick = {
                        navActions.navigateToStudentCreatorRoute(
                            schoolClassId = schoolClassId,
                            studentId = studentId
                        )
                    },
                ),
                ActionMenuItem(
                    name = "",
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    onClick = {
                        viewModel.deleteStudent()
                    },
                ),
            )

            addActionMenuItems(menuItems)

            onDispose {
                removeActionMenuItems(menuItems)
            }
        }

        StudentScreen(
            studentResource = studentResource,
            onEmailClick = {},
            onPhoneClick = {},
            onGradeClick = {},
            onAddGradeClick = {},
            onNoteClick = {},
            onAddNoteClick = {},
            isGradesExpanded = viewModel.isGradesExpanded,
            isNotesExpanded = viewModel.isNotesExpanded,
            isStudentDeleted = viewModel.isStudentDeleted,
        )
    }

    composable(
        TeacherDestinations.STUDENT_CREATOR_ROUTE,
        arguments = listOf(
            navArgument(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG) {
                type = NavType.LongType
            },
            navArgument(TeacherDestinationsArgs.STUDENT_ID_ARG) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) {
        val viewModel: StudentCreatorViewModel = hiltViewModel()
        val studentResource by viewModel.studentResource.collectAsStateWithLifecycle()
        val form = viewModel.form
        val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()

        LaunchedEffect(schoolClassName) {
            val name = schoolClassName.orEmpty()
            setTitle("Klasa $name")
        }

        StudentCreatorScreen(
            studentResource = studentResource,
            formStatus = form.status,
            name = form.name,
            onNameChange = viewModel::onNameChange,
            surname = form.surname,
            onSurnameChange = viewModel::onSurnameChange,
            email = form.email,
            onEmailChange = viewModel::onEmailChange,
            phone = form.phone,
            onPhoneChange = viewModel::onPhoneChange,
            isValid = form.isValid,
            onAddStudent = viewModel::onSubmit,
            onStudentAdd = { navController.popBackStack() },
        )
    }
}