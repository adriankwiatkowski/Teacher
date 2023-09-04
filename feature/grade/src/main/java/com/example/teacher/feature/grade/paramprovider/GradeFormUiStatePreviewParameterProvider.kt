package com.example.teacher.feature.grade.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.BasicStudentPreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.GradeTemplateInfoPreviewParameterProvider
import com.example.teacher.feature.grade.data.GradeFormUiState

internal class GradeFormUiStatePreviewParameterProvider :
    PreviewParameterProvider<GradeFormUiState> {
    override val values: Sequence<GradeFormUiState> = sequenceOf(
        GradeFormUiState(
            gradeTemplateInfo = GradeTemplateInfoPreviewParameterProvider().values.first(),
            student = BasicStudentPreviewParameterProvider().values.first(),
        )
    )
}