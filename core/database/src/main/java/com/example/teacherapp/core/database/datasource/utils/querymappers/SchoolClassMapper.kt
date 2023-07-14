package com.example.teacherapp.core.database.datasource.utils.querymappers

import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.SchoolClass
import java.time.LocalDate

internal object SchoolClassMapper {

    fun mapSchoolClass(
        id: Long,
        schoolClassName: String,
        schoolYearId: Long,
        schoolYearName: String,
        termFirstId: Long,
        termSecondId: Long,
        termFirstName: String,
        termFirstStartDate: LocalDate,
        termFirstEndDate: LocalDate,
        termSecondName: String,
        termSecondStartDate: LocalDate,
        termSecondEndDate: LocalDate,
    ): SchoolClass {
        return SchoolClass(
            id = id,
            name = schoolClassName,
            schoolYear = SchoolYearMapper.mapSchoolYear(
                id = schoolYearId,
                schoolYearName = schoolYearName,
                termFirstId = termFirstId,
                termSecondId = termSecondId,
                termFirstName = termFirstName,
                termFirstStartDate = termFirstStartDate,
                termFirstEndDate = termFirstEndDate,
                termSecondName = termSecondName,
                termSecondStartDate = termSecondStartDate,
                termSecondEndDate = termSecondEndDate,
            ),
            students = emptyList(),
            lessons = emptyList(),
        )
    }

    fun mapBasicSchoolClass(id: Long, name: String): BasicSchoolClass {
        // TODO: Query student count.
        return BasicSchoolClass(
            id = id,
            name = name,
            studentCount = 0,
        )
    }
}