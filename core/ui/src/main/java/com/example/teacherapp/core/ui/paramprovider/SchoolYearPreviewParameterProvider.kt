package com.example.teacherapp.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.model.data.Term

class SchoolYearsPreviewParameterProvider : PreviewParameterProvider<List<SchoolYear>> {
    override val values: Sequence<List<SchoolYear>> = sequenceOf(
        SchoolYearPreviewParameterProvider().values.toList(),
    )
}

class SchoolYearPreviewParameterProvider : PreviewParameterProvider<SchoolYear> {
    override val values: Sequence<SchoolYear> = sequenceOf(getSchoolYear())

    private fun getSchoolYear(): SchoolYear {
        val currentDate = TimeUtils.currentDate()
        val year = TimeUtils.localDateYear(currentDate)

        return SchoolYear(
            id = 1L,
            name = "Rok $year/${year + 1}",
            firstTerm = Term(
                id = 1L,
                name = "I",
                startDate = currentDate,
                endDate = TimeUtils.plusDays(currentDate, 1L),
            ),
            secondTerm = Term(
                id = 2L,
                name = "II",
                startDate = TimeUtils.plusDays(currentDate, 2L),
                endDate = TimeUtils.plusDays(currentDate, 3L),
            ),
        )
    }
}