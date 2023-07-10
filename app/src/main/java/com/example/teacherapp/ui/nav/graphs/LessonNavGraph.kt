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
import com.example.teacherapp.ui.screens.lesson.LessonCreatorScreen
import com.example.teacherapp.ui.screens.lesson.data.LessonCreatorViewModel

fun NavGraphBuilder.addLessonGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
) {
    composable(
        TeacherDestinations.LESSON_CREATOR_ROUTE,
        arguments = listOf(
            navArgument(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG) {
                type = NavType.LongType
            },
            navArgument(TeacherDestinationsArgs.LESSON_ID_ARG) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) {
        val viewModel: LessonCreatorViewModel = hiltViewModel()
        val lessonResource by viewModel.lessonResource.collectAsStateWithLifecycle()
        val form = viewModel.form
        val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()

        LaunchedEffect(schoolClassName) {
            val name = schoolClassName.orEmpty()
            setTitle("Klasa $name")
        }

        LessonCreatorScreen(
            lessonResource = lessonResource,
            formStatus = form.status,
            name = form.name,
            onNameChange = viewModel::onNameChange,
            isValid = form.isValid,
            schoolClassName = schoolClassName.orEmpty(),
            onAddLesson = viewModel::onSubmit,
            onLessonAdd = navController::popBackStack,
        )
    }
}