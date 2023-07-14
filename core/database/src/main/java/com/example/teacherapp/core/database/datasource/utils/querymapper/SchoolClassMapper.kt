package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lesson.GetLessonsBySchoolClassId
import com.example.teacherapp.core.database.generated.queries.schoolclass.GetAllSchoolClasses
import com.example.teacherapp.core.database.generated.queries.schoolclass.GetSchoolClassById
import com.example.teacherapp.core.database.generated.queries.student.GetStudentsBySchoolClassId
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.model.data.Term

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
                classId = student.school_class_id,
                orderInClass = student.order_in_class,
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

internal fun toExternal(schoolClasses: List<GetAllSchoolClasses>): List<BasicSchoolClass> =
    schoolClasses.map { schoolClass ->
        BasicSchoolClass(
            id = schoolClass.id,
            name = schoolClass.name,
            studentCount = 0, // TODO: Query student count.
        )
    }