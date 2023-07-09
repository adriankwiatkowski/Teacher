package com.example.teacherapp.ui.screens.paramproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.data.models.entities.SchoolYear
import com.example.teacherapp.data.models.entities.Term
import java.time.LocalDate

class SchoolYearsPreviewParameterProvider : PreviewParameterProvider<List<SchoolYear>> {
    override val values: Sequence<List<SchoolYear>> = sequenceOf(
        SchoolYearPreviewParameterProvider().values.toList(),
    )
}

class SchoolYearPreviewParameterProvider : PreviewParameterProvider<SchoolYear> {
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