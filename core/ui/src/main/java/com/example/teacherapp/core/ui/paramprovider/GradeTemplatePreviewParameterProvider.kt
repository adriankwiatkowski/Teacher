package com.example.teacherapp.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.core.model.data.GradeTemplate

class GradeTemplatePreviewParameterProvider : PreviewParameterProvider<GradeTemplate> {
    override val values: Sequence<GradeTemplate> = sequenceOf(
        GradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            description = "Sprawdzian z dodawania",
            weight = 1,
        )
    )
}

class BasicGradeTemplatesPreviewParameterProvider :
    PreviewParameterProvider<List<BasicGradeTemplate>> {
    override val values: Sequence<List<BasicGradeTemplate>> = sequenceOf(
        BasicGradeTemplatePreviewParameterProvider().values.toList(),
    )
}

class BasicGradeTemplatePreviewParameterProvider : PreviewParameterProvider<BasicGradeTemplate> {
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