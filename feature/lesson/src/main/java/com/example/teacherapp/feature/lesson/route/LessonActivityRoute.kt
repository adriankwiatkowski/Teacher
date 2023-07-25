package com.example.teacherapp.feature.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.lesson.lessonactivity.LessonActivityScreen
import com.example.teacherapp.feature.lesson.lessonactivity.data.LessonActivityViewModel

@Composable
internal fun LessonActivityRoute(
    viewModel: LessonActivityViewModel = hiltViewModel(),
) {
    val lessonActivitiesResult by viewModel.lessonActivitiesResult.collectAsStateWithLifecycle()

    LessonActivityScreen(
        lessonActivitiesResult = lessonActivitiesResult,
        onIncreaseLessonActivity = viewModel::onIncreaseLessonActivity,
        onDecreaseLessonActivity = viewModel::onDecreaseLessonActivity,
    )
}