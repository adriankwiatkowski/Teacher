package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.lesson.LessonFormScreen
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.data.LessonFormViewModel

@Composable
internal fun LessonFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    viewModel: LessonFormViewModel = hiltViewModel(),
) {
    val lessonResult by viewModel.lessonResult.collectAsStateWithLifecycle()
    val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()
    val form = viewModel.form
    val formStatus = form.status

    // Observe save.
    LaunchedEffect(formStatus) {
        if (formStatus == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.lesson_lesson_saved)
            onNavBack()
        }
    }

    // TODO: Handle back press to prevent accidentally closing form.

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