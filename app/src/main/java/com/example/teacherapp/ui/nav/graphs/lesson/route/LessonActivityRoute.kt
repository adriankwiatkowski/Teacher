package com.example.teacherapp.ui.nav.graphs.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.lessonactivity.LessonActivityScreen
import com.example.teacherapp.ui.screens.lessonactivity.data.LessonActivityViewModel

@Composable
fun LessonActivityRoute(
    viewModel: LessonActivityViewModel = hiltViewModel(),
) {
    val lessonActivitiesResult by viewModel.lessonActivitiesResult.collectAsStateWithLifecycle()

    LessonActivityScreen(
        lessonActivitiesResult = lessonActivitiesResult,
        onIncreaseLessonActivity = viewModel::onIncreaseLessonActivity,
        onDecreaseLessonActivity = viewModel::onDecreaseLessonActivity,
    )
}