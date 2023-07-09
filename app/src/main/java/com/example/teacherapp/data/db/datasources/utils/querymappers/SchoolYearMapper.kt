package com.example.teacherapp.data.db.datasources.utils.querymappers

import com.example.teacherapp.data.models.entities.SchoolYear
import com.example.teacherapp.data.models.entities.Term
import java.time.LocalDate

object SchoolYearMapper {

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