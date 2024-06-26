package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.GradeTemplateInfo

class GradeTemplateInfoPreviewParameterProvider : PreviewParameterProvider<GradeTemplateInfo> {
    override val values: Sequence<GradeTemplateInfo> = sequenceOf(
        GradeTemplateInfo(
            gradeTemplateId = 1L,
            gradeName = "Dodawanie",
            gradeDescription = "Dodawanie liczb naturalnych",
            gradeWeight = 3,
            isFirstTerm = true,
            averageGrade = DecimalUtils.Five,
            lesson = LessonPreviewParameterProvider().values.first(),
        ),
    )
}

class BasicGradesForTemplatePreviewParameterProvider :
    PreviewParameterProvider<List<BasicGradeForTemplate>> {
    override val values: Sequence<List<BasicGradeForTemplate>> = sequenceOf(
        listOf(
            BasicGradeForTemplate(
                id = 1L,
                grade = DecimalUtils.Six,
                date = TimeUtils.currentDate(),
                studentId = 1L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 2L,
                grade = DecimalUtils.Five,
                date = TimeUtils.currentDate(),
                studentId = 2L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 3L,
                grade = DecimalUtils.Four,
                date = TimeUtils.currentDate(),
                studentId = 3L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 4L,
                grade = DecimalUtils.Three,
                date = TimeUtils.currentDate(),
                studentId = 4L,
                studentFullName = "Jan Kowalski",
                gradeName = "Dodawanie",
                gradeWeight = 1,
                isFirstTerm = true,
            ),
            BasicGradeForTemplate(
                id = 5L,
                grade = DecimalUtils.Two,
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