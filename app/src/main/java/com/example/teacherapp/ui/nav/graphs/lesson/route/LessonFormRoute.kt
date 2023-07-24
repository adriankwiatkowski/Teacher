package com.example.teacherapp.ui.nav.graphs.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.ui.screens.lesson.LessonFormScreen
import com.example.teacherapp.ui.screens.lesson.data.LessonFormViewModel

@Composable
internal fun LessonFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    viewModel: LessonFormViewModel = hiltViewModel(),
) {
    val lessonResult by viewModel.lessonResult.collectAsStateWithLifecycle()
    val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()
    val form = viewModel.form
    val formStatus = form.status

    LaunchedEffect(formStatus, onShowSnackbar, onNavBack) {
        if (formStatus == FormStatus.Success) {
            onShowSnackbar("Zapisano zajęcia")
            onNavBack()
        }
    }

    LessonFormScreen(
        lessonResult = lessonResult,
        formStatus = form.status,
        name = form.name,
        onNameChange = viewModel::onNameChange,
        isSubmitEnabled = form.isSubmitEnabled,
        schoolClassName = schoolClassName.orEmpty(),
        onAddLessonClick = viewModel::onSubmit,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
    )
}