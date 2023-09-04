package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.core.model.data.GradeTemplate

class GradeTemplatePreviewParameterProvider : PreviewParameterProvider<GradeTemplate> {
    override val values: Sequence<GradeTemplate> = sequenceOf(
        GradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            description = "Sprawdzian z dodawania",
            weight = 1,
            isFirstTerm = true,
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
            isFirstTerm = true,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = false,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = true,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = false,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = true,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = false,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = true,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = false,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = true,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = false,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = true,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = false,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = true,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = false,
        ),
    ).mapIndexed { index, basicGradeTemplate -> basicGradeTemplate.copy(id = index + 1L) }
}