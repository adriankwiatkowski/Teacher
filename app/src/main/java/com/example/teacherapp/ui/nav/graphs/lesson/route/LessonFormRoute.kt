package com.example.teacherapp.ui.nav.graphs.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.lesson.LessonFormScreen
import com.example.teacherapp.ui.screens.lesson.data.LessonFormViewModel

@Composable
internal fun LessonFormRoute(
    onNavBack: () -> Unit,
    setTitle: (String) -> Unit,
    viewModel: LessonFormViewModel = hiltViewModel(),
) {
    val lessonResource by viewModel.lessonResource.collectAsStateWithLifecycle()
    val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()
    val form = viewModel.form

    LaunchedEffect(schoolClassName) {
        val name = schoolClassName.orEmpty()
        setTitle("Klasa $name")
    }

    LessonFormScreen(
        lessonResource = lessonResource,
        formStatus = form.status,
        name = form.name,
        onNameChange = viewModel::onNameChange,
        isValid = form.isValid,
        schoolClassName = schoolClassName.orEmpty(),
        onAddLesson = viewModel::onSubmit,
        onLessonAdd = onNavBack,
    )
}