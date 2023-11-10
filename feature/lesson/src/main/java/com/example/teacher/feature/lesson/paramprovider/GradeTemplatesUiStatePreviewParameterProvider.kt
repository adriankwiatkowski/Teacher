package com.example.teacher.feature.lesson.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.BasicGradeTemplatesPreviewParameterProvider
import com.example.teacher.feature.lesson.gradetemplate.data.GradeTemplatesUiState

internal class GradeTemplatesUiStatePreviewParameterProvider :
    PreviewParameterProvider<GradeTemplatesUiState> {

    override val values: Sequence<GradeTemplatesUiState>
        get() {
            val grades = BasicGradeTemplatesPreviewParameterProvider().values.first()

            return sequenceOf(
                GradeTemplatesUiState(
                    firstTermGrades = grades.filter { grade -> grade.isFirstTerm },
                    secondTermGrades = grades.filter { grade -> !grade.isFirstTerm },
                ),
                GradeTemplatesUiState(
                    firstTermGrades = grades,
                    secondTermGrades = emptyList(),
                ),
                GradeTemplatesUiState(
                    firstTermGrades = emptyList(),
                    secondTermGrades = emptyList(),
                )
            )
        }
}