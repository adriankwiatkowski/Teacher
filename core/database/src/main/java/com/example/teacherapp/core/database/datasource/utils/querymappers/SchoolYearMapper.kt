package com.example.teacherapp.core.database.datasource.utils.querymappers

import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.model.data.Term
import java.time.LocalDate

internal object SchoolYearMapper {

    fun mapSchoolYear(
        id: Long,
        schoolYearName: String,
        termFirstId: Long,
        termSecondId: Long,
        termFirstName: String,
        termFirstStartDate: LocalDate,
        termFirstEndDate: LocalDate,
        termSecondName: String,
        termSecondStartDate: LocalDate,
        termSecondEndDate: LocalDate,
    ): SchoolYear = SchoolYear(
        id = id,
        name = schoolYearName,
        firstTerm = Term(
            id = termFirstId,
            name = termFirstName,
            startDate = termFirstStartDate,
            endDate = termFirstEndDate,
        ),
        secondTerm = Term(
            id = termSecondId,
            name = termSecondName,
            startDate = termSecondStartDate,
            endDate = termSecondEndDate,
        ),
    )
}