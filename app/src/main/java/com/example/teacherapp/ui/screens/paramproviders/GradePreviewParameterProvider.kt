package com.example.teacherapp.ui.screens.paramproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicGrade
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.Grade
import java.math.BigDecimal
import java.time.LocalDate

class GradePreviewParameterProvider : PreviewParameterProvider<Grade> {
    override val values: Sequence<Grade> = sequenceOf(
        Grade(
            id = 1L,
            grade = BigDecimal("4.75"),
            date = LocalDate.now(),
            studentId = 1L,
            lesson = LessonPreviewParameterProvider().values.first(),
            gradeTemplate = GradeTemplatePreviewParameterProvider().values.first(),
        )
    )
}

class BasicGradesForTemplatePreviewParameterProvider :
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

class BasicGradesPreviewParameterProvider : PreviewParameterProvider<List<BasicGrade>> {
    override val values: Sequence<List<BasicGrade>> = sequenceOf(
        BasicGradePreviewParameterProvider().values.toList(),
    )
}

class BasicGradePreviewParameterProvider : PreviewParameterProvider<BasicGrade> {
    override val values: Sequence<BasicGrade> = sequenceOf(
        BasicGrade(
            id = 1L,
            grade = BigDecimal("4.75"),
            date = LocalDate.now(),
            studentId = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
        ),
        BasicGrade(
            id = 2L,
            grade = BigDecimal("5.0"),
            date = LocalDate.now(),
            studentId = 2L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
        ),
    )
}