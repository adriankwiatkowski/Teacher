package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.schoolclass.GetAllSchoolYears
import com.example.teacherapp.core.database.generated.queries.schoolclass.GetSchoolYearById
import com.example.teacherapp.core.database.generated.queries.schoolclass.GetSchoolYearByLessonId
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.model.data.Term

internal fun toExternal(
    schoolYears: List<GetAllSchoolYears>
): List<SchoolYear> = schoolYears.map { schoolYear ->
    SchoolYear(
        id = schoolYear.id,
        name = schoolYear.school_year_name,
        firstTerm = Term(
            id = schoolYear.term_first_id,
            name = schoolYear.term_first_name,
            startDate = schoolYear.term_first_start_date,
            endDate = schoolYear.term_first_end_date,
        ),
        secondTerm = Term(
            id = schoolYear.term_second_id,
            name = schoolYear.term_second_name,
            startDate = schoolYear.term_second_start_date,
            endDate = schoolYear.term_second_end_date,
        ),
    )
}

internal fun toExternal(schoolYear: GetSchoolYearById?): SchoolYear? = run {
    if (schoolYear == null) {
        return@run null
    }

    SchoolYear(
        id = schoolYear.id,
        name = schoolYear.school_year_name,
        firstTerm = Term(
            id = schoolYear.term_first_id,
            name = schoolYear.term_first_name,
            startDate = schoolYear.term_first_start_date,
            endDate = schoolYear.term_first_end_date,
        ),
        secondTerm = Term(
            id = schoolYear.term_second_id,
            name = schoolYear.term_second_name,
            startDate = schoolYear.term_second_start_date,
            endDate = schoolYear.term_second_end_date,
        ),
    )
}

internal fun toExternal(schoolYear: GetSchoolYearByLessonId?): SchoolYear? = run {
    if (schoolYear == null) {
        return@run null
    }

    SchoolYear(
        id = schoolYear.id,
        name = schoolYear.school_year_name,
        firstTerm = Term(
            id = schoolYear.term_first_id,
            name = schoolYear.term_first_name,
            startDate = schoolYear.term_first_start_date,
            endDate = schoolYear.term_first_end_date,
        ),
        secondTerm = Term(
            id = schoolYear.term_second_id,
            name = schoolYear.term_second_name,
            startDate = schoolYear.term_second_start_date,
            endDate = schoolYear.term_second_end_date,
        ),
    )
}