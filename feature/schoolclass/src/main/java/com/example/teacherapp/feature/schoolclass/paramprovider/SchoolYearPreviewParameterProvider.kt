package com.example.teacherapp.feature.schoolclass.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.model.data.Term
import java.time.LocalDate

internal class SchoolYearsPreviewParameterProvider : PreviewParameterProvider<List<SchoolYear>> {
    override val values: Sequence<List<SchoolYear>> = sequenceOf(
        SchoolYearPreviewParameterProvider().values.toList(),
    )
}

internal class SchoolYearPreviewParameterProvider : PreviewParameterProvider<SchoolYear> {
    override val values: Sequence<SchoolYear> = sequenceOf(
        SchoolYear(
            id = 1L,
            name = "Rok ${LocalDate.now().year}/${LocalDate.now().year + 1}",
            firstTerm = Term(
                id = 1L,
                name = "I",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(1L),
            ),
            secondTerm = Term(
                id = 2L,
                name = "II",
                startDate = LocalDate.now().plusDays(2L),
                endDate = LocalDate.now().plusDays(3L),
            ),
        ),
    )
}