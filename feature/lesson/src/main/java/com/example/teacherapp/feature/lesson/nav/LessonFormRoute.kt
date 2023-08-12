package com.example.teacherapp.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.lesson.LessonFormScreen
import com.example.teacherapp.feature.lesson.data.LessonFormViewModel

@Composable
internal fun LessonFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
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
        snackbarHostState = snackbarHostState,
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