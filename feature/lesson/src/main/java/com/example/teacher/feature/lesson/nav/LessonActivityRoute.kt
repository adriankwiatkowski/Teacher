package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.feature.lesson.lessonactivity.LessonActivityScreen
import com.example.teacher.feature.lesson.lessonactivity.data.LessonActivityViewModel

@Composable
internal fun LessonActivityRoute(
    snackbarHostState: SnackbarHostState,
    viewModel: LessonActivityViewModel = hiltViewModel(),
) {
    val lessonActivitiesResult by viewModel.lessonActivitiesResult.collectAsStateWithLifecycle()

    LessonActivityScreen(
        lessonActivitiesResult = lessonActivitiesResult,
        snackbarHostState = snackbarHostState,
        onIncreaseLessonActivity = viewModel::onIncreaseLessonActivity,
        onDecreaseLessonActivity = viewModel::onDecreaseLessonActivity,
    )
}