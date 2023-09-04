package com.example.teacher.feature.schedule.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.feature.schedule.LessonPickerScreen
import com.example.teacher.feature.schedule.data.LessonPickerViewModel

@Composable
internal fun LessonPickerRoute(
    showNavigationIcon: Boolean,
    snackbarHostState: SnackbarHostState,
    onNavBack: () -> Unit,
    onLessonClick: (lessonId: Long) -> Unit,
    viewModel: LessonPickerViewModel = hiltViewModel(),
) {
    val lessonResult by viewModel.lessonsResult.collectAsStateWithLifecycle()

    LessonPickerScreen(
        lessonsResult = lessonResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        onLessonClick = onLessonClick,
    )
}