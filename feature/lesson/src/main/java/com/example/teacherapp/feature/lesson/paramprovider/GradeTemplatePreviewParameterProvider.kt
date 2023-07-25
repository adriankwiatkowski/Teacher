package com.example.teacherapp.feature.lesson.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicGradeTemplate

internal class BasicGradeTemplatesPreviewParameterProvider :
    PreviewParameterProvider<List<BasicGradeTemplate>> {
    override val values: Sequence<List<BasicGradeTemplate>> = sequenceOf(
        BasicGradeTemplatePreviewParameterProvider().values.toList(),
    )
}

internal class BasicGradeTemplatePreviewParameterProvider :
    PreviewParameterProvider<BasicGradeTemplate> {
    override val values: Sequence<BasicGradeTemplate> = sequenceOf(
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
        ),
    )
}