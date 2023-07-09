package com.example.teacherapp.data.db.datasources.utils.querymappers

import com.example.teacherapp.data.models.entities.BasicSchoolClass
import com.example.teacherapp.data.models.entities.SchoolClass
import java.time.LocalDate

object SchoolClassMapper {

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