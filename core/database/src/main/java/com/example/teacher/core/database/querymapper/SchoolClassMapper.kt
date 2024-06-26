package com.example.teacher.core.database.querymapper

import com.example.teacher.core.database.generated.queries.lesson.GetLessonsBySchoolClassId
import com.example.teacher.core.database.generated.queries.schoolclass.GetAllSchoolClasses
import com.example.teacher.core.database.generated.queries.schoolclass.GetSchoolClassById
import com.example.teacher.core.database.generated.queries.student.GetStudentsBySchoolClassId
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.core.model.data.SchoolClassesByYear
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term

internal fun toExternal(
    schoolClass: GetSchoolClassById?,
    students: List<GetStudentsBySchoolClassId>,
    lessons: List<GetLessonsBySchoolClassId>,
): SchoolClass? {
    if (schoolClass == null) {
        return null
    }

    return SchoolClass(
        id = schoolClass.id,
        name = schoolClass.school_class_name,
        schoolYear = SchoolYear(
            id = schoolClass.school_year_id,
            name = schoolClass.school_year_name,
            firstTerm = Term(
                id = schoolClass.term_first_id,
                name = schoolClass.term_first_name,
                startDate = schoolClass.term_first_start_date,
                endDate = schoolClass.term_first_end_date,
            ),
            secondTerm = Term(
                id = schoolClass.term_second_id,
                name = schoolClass.term_second_name,
                startDate = schoolClass.term_second_start_date,
                endDate = schoolClass.term_second_end_date,
            ),
        ),
        students = students.map { student ->
            BasicStudent(
                id = student.id,
                schoolClassId = student.school_class_id,
                registerNumber = student.register_number,
                name = student.name,
                surname = student.surname,
                email = student.email,
                phone = student.phone,
            )
        },
        lessons = lessons.map { lesson ->
            BasicLesson(
                id = lesson.id,
                name = lesson.name,
                schoolClassId = lesson.school_class_id,
            )
        },
    )
}

internal fun toExternal(schoolClass: GetSchoolClassById?): BasicSchoolClass? {
    if (schoolClass == null) {
        return null
    }

    return BasicSchoolClass(
        id = schoolClass.id,
        name = schoolClass.school_class_name,
        schoolYear = SchoolYear(
            id = schoolClass.school_year_id,
            name = schoolClass.school_year_name,
            firstTerm = Term(
                id = schoolClass.term_first_id,
                name = schoolClass.term_first_name,
                startDate = schoolClass.term_first_start_date,
                endDate = schoolClass.term_first_end_date,
            ),
            secondTerm = Term(
                id = schoolClass.term_second_id,
                name = schoolClass.term_second_name,
                startDate = schoolClass.term_second_start_date,
                endDate = schoolClass.term_second_end_date,
            ),
        ),
        studentCount = schoolClass.student_count,
        lessonCount = schoolClass.lesson_count,
    )
}

internal fun toExternal(
    schoolClasses: List<GetAllSchoolClasses>
): List<SchoolClassesByYear> = schoolClasses
    .groupBy { schoolYear -> schoolYear.school_year_id }
    .map { (_, schoolClasses) ->
        val schoolYear = schoolClasses.first().let { schoolClass ->
            SchoolYear(
                id = schoolClass.school_year_id,
                name = schoolClass.school_year_name,
                firstTerm = Term(
                    id = schoolClass.term_first_id,
                    name = schoolClass.term_first_name,
                    startDate = schoolClass.term_first_start_date,
                    endDate = schoolClass.term_first_end_date,
                ),
                secondTerm = Term(
                    id = schoolClass.term_second_id,
                    name = schoolClass.term_second_name,
                    startDate = schoolClass.term_second_start_date,
                    endDate = schoolClass.term_second_end_date,
                ),
            )
        }

        SchoolClassesByYear(
            year = schoolYear,
            schoolClasses = schoolClasses.map { schoolClass ->
                BasicSchoolClass(
                    id = schoolClass.id,
                    name = schoolClass.name,
                    schoolYear = schoolYear,
                    studentCount = schoolClass.student_count,
                    lessonCount = schoolClass.lesson_count,
                )
            }
        )
    }