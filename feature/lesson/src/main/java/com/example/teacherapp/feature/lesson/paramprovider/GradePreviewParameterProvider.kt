package com.example.teacherapp.feature.lesson.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import com.example.teacherapp.feature.lesson.grade.data.GradeFormUiState
import java.math.BigDecimal
import java.time.LocalDate

internal class GradeFormUiStatePreviewParameterProvider :
    PreviewParameterProvider<GradeFormUiState> {
    override val values: Sequence<GradeFormUiState> = sequenceOf(
        GradeFormUiState(
            gradeTemplateInfo = GradeTemplateInfoPreviewParameterProvider().values.first(),
            student = BasicStudentPreviewParameterProvider().values.first(),
        )
    )
}

internal class GradeTemplateInfoPreviewParameterProvider :
    PreviewParameterProvider<GradeTemplateInfo> {
    override val values: Sequence<GradeTemplateInfo> = sequenceOf(
        GradeTemplateInfo(
            gradeTemplateId = 1L,
            gradeName = "Dodawanie",
            gradeWeight = 3,
            lessonId = 1L,
            lessonName = "Matematyka",
            schoolClassId = 1L,
            schoolClassName = "1A",
        ),
    )
}

internal class BasicGradesForTemplatePreviewParameterProvider :
    PreviewParameterProvider<List<BasicGradeForTemplate>> {
    override val values: Sequence<List<BasicGradeForTemplate>> = sequenceOf(
        listOf(
            BasicGradeForTemplate(
                id = 1L,
                grade = BigDecimal("6.00"),
                date = LocalDate.now(),
                studentId = 1L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
            ),
            BasicGradeForTemplate(
                id = 2L,
                grade = BigDecimal("5.00"),
                date = LocalDate.now(),
                studentId = 2L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
            ),
            BasicGradeForTemplate(
                id = 3L,
                grade = BigDecimal("4.00"),
                date = LocalDate.now(),
                studentId = 3L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
            ),
            BasicGradeForTemplate(
                id = 4L,
                grade = BigDecimal("3.00"),
                date = LocalDate.now(),
                studentId = 4L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
            ),
            BasicGradeForTemplate(
                id = 5L,
                grade = BigDecimal("2.00"),
                date = LocalDate.now(),
                studentId = 5L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
            ),
            BasicGradeForTemplate(
                id = null,
                grade = null,
                date = null,
                studentId = 6L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
            ),
        ),
    )
}