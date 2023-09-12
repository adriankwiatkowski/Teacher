package com.example.teacher.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.feature.schoolclass.SchoolClassLessonsScreen

@Composable
internal fun SchoolClassLessonsRoute(
    snackbarHostState: SnackbarHostState,
    lessons: List<BasicLesson>,
    onLessonClick: (id: Long) -> Unit,
    onAddLessonClick: () -> Unit,
) {
    SchoolClassLessonsScreen(
        snackbarHostState = snackbarHostState,
        lessons = lessons,
        onLessonClick = onLessonClick,
        onAddLessonClick = onAddLessonClick,
    )
}