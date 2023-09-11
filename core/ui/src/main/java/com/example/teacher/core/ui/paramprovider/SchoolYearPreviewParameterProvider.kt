package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term

class SchoolYearsPreviewParameterProvider : PreviewParameterProvider<List<SchoolYear>> {
    override val values: Sequence<List<SchoolYear>> = sequenceOf(
        SchoolYearPreviewParameterProvider().values.toList(),
    )
}

class SchoolYearPreviewParameterProvider : PreviewParameterProvider<SchoolYear> {
    override val values: Sequence<SchoolYear> = sequenceOf(
        getSchoolYear(2),
        getSchoolYear(1),
        getSchoolYear(0),
    )

    private fun getSchoolYear(minusYears: Long): SchoolYear {
        val currentDate = TimeUtils.minusYears(TimeUtils.currentDate(), minusYears)
        val year = TimeUtils.localDateYear(currentDate)

        return SchoolYear(
            id = minusYears,
            name = "Rok $year/${year + 1}",
            firstTerm = Term(
                id = minusYears * 2 + 1L,
                name = "I",
                startDate = currentDate,
                endDate = TimeUtils.plusDays(currentDate, 1L),
            ),
            secondTerm = Term(
                id = minusYears * 2 + 2L,
                name = "II",
                startDate = TimeUtils.plusDays(currentDate, 2L),
                endDate = TimeUtils.plusDays(currentDate, 3L),
            ),
        )
    }
}