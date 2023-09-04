package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.BasicGrade
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.Grade
import com.example.teacher.core.model.data.GradeTemplateInfo
import java.math.BigDecimal

class GradeTemplateInfoPreviewParameterProvider : PreviewParameterProvider<GradeTemplateInfo> {
    override val values: Sequence<GradeTemplateInfo> = sequenceOf(
        GradeTemplateInfo(
            gradeTemplateId = 1L,
            gradeName = "Dodawanie",
            gradeWeight = 3,
            lessonId = 1L,
            lessonName = "Matematyka",
            schoolClassId = 1L,
            schoolClassName = "1A",
            isFirstTerm = true,
        ),
    )
}

class GradePreviewParameterProvider : PreviewParameterProvider<Grade> {
    override val values: Sequence<Grade> = sequenceOf(
        Grade(
            id = 1L,
            grade = BigDecimal("4.75"),
            date = TimeUtils.currentDate(),
            studentId = 1L,
            studentFullName = "Jan Kowalski",
            lessonId = 1L,
            lessonName = "Matematyka",
            schoolClassId = 1L,
            schoolClassName = "1A",
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
                date = TimeUtils.currentDate(),
                studentId = 1L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 2L,
                grade = BigDecimal("5.00"),
                date = TimeUtils.currentDate(),
                studentId = 2L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 3L,
                grade = BigDecimal("4.00"),
                date = TimeUtils.currentDate(),
                studentId = 3L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 4L,
                grade = BigDecimal("3.00"),
                date = TimeUtils.currentDate(),
                studentId = 4L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 5L,
                grade = BigDecimal("2.00"),
                date = TimeUtils.currentDate(),
                studentId = 5L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = false,
            ),
            BasicGradeForTemplate(
                id = null,
                grade = null,
                date = null,
                studentId = 6L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = false,
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
            date = TimeUtils.currentDate(),
            studentId = 1L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = true,
        ),
        BasicGrade(
            id = 2L,
            grade = BigDecimal("5.0"),
            date = TimeUtils.currentDate(),
            studentId = 2L,
            lessonId = 1L,
            name = "Dodawanie",
            weight = 1,
            isFirstTerm = false,
        ),
    )
}