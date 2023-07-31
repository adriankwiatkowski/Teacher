package com.example.teacherapp.feature.schedule.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.schedule.LessonPickerScreen
import com.example.teacherapp.feature.schedule.data.LessonPickerViewModel

@Composable
internal fun LessonPickerRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onLessonClick: (lessonId: Long) -> Unit,
    viewModel: LessonPickerViewModel = hiltViewModel(),
) {
    val lessonResult by viewModel.lessonsResult.collectAsStateWithLifecycle()

    LessonPickerScreen(
        lessonsResult = lessonResult,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        onLessonClick = onLessonClick,
    )
}