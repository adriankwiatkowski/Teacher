package com.example.teacher.feature.lesson.lessonactivity.data

import com.example.teacher.core.model.data.LessonActivity

internal data class LessonActivityUiState(
    val firstTermLessonActivities: List<LessonActivity>,
    val secondTermLessonActivities: List<LessonActivity>,
)