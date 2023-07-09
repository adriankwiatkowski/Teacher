package com.example.teacherapp.ui.nav.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import com.example.teacherapp.ui.screens.student.StudentCreatorScreen
import com.example.teacherapp.ui.screens.student.data.StudentCreatorViewModel

fun NavGraphBuilder.addStudentRouteGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
) {
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
            setTitle("Klasa ${schoolClassName ?: ""}")
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
            schoolClassName = schoolClassName.orEmpty(),
            onAddStudent = viewModel::onSubmit,
            onStudentAdd = { navController.popBackStack() },
        )
    }
}