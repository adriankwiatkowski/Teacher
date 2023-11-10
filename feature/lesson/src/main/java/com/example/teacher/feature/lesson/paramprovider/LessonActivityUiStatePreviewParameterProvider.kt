package com.example.teacher.feature.lesson.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.LessonActivitiesPreviewParameterProvider
import com.example.teacher.feature.lesson.lessonactivity.data.LessonActivityUiState

internal class LessonActivityUiStatePreviewParameterProvider :
    PreviewParameterProvider<LessonActivityUiState> {

    override val values: Sequence<LessonActivityUiState>
        get() {
            val lessonActivities = LessonActivitiesPreviewParameterProvider().values.first()

            return sequenceOf(
                LessonActivityUiState(
                    firstTermLessonActivities = lessonActivities.filter { it.isFirstTerm },
                    secondTermLessonActivities = lessonActivities.filter { !it.isFirstTerm },
                ),
                LessonActivityUiState(
                    firstTermLessonActivities = emptyList(),
                    secondTermLessonActivities = emptyList(),
                ),
            )
        }
}