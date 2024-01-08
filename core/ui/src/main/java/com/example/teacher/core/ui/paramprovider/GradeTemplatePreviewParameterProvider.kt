package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.model.data.BasicGradeTemplate

class BasicGradeTemplatesPreviewParameterProvider :
    PreviewParameterProvider<List<BasicGradeTemplate>> {
    override val values: Sequence<List<BasicGradeTemplate>> = sequenceOf(
        BasicGradeTemplatePreviewParameterProvider().values.toList(),
        emptyList(),
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
            averageGrade = null,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Odejmowanie",
            weight = 2,
            isFirstTerm = true,
            averageGrade = DecimalUtils.Two,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Mnożenie",
            weight = 3,
            isFirstTerm = true,
            averageGrade = DecimalUtils.Three,
        ),
        BasicGradeTemplate(
            id = 2L,
            lessonId = 1L,
            name = "Dzielenie",
            weight = 4,
            isFirstTerm = true,
            averageGrade = DecimalUtils.Four,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Trygonometria",
            weight = 5,
            isFirstTerm = false,
            averageGrade = DecimalUtils.Five,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Pochodne",
            weight = 6,
            isFirstTerm = false,
            averageGrade = DecimalUtils.Six,
        ),
        BasicGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Całki",
            weight = 1,
            isFirstTerm = false,
            averageGrade = DecimalUtils.calculateArithmeticAverage(
                listOf(
                    DecimalUtils.Three,
                    DecimalUtils.Six,
                ),
            )!!,
        ),
    ).mapIndexed { index, basicGradeTemplate -> basicGradeTemplate.copy(id = index + 1L) }
}