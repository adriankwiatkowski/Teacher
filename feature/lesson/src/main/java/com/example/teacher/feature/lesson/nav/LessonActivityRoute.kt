package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.feature.lesson.lessonactivity.LessonActivityScreen
import com.example.teacher.feature.lesson.lessonactivity.data.LessonActivityViewModel

@Composable
internal fun LessonActivityRoute(
    snackbarHostState: SnackbarHostState,
    lesson: Lesson,
    viewModel: LessonActivityViewModel = hiltViewModel(),
) {
    val lessonActivityUiStateResult by viewModel.lessonActivityUiStateResult.collectAsStateWithLifecycle()

    LessonActivityScreen(
        lessonActivityUiStateResult = lessonActivityUiStateResult,
        lesson = lesson,
        snackbarHostState = snackbarHostState,
        onIncreaseLessonActivity = viewModel::onIncreaseLessonActivity,
        onDecreaseLessonActivity = viewModel::onDecreaseLessonActivity,
    )
}