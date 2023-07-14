package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.student.GetStudentById
import com.example.teacherapp.core.database.generated.queries.student.GetStudentsBySchoolClassId
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Student

internal fun toExternal(student: GetStudentById?): Student? = run {
    if (student == null) {
        return@run null
    }

    val schoolClass = BasicSchoolClass(
        id = student.school_class_id,
        name = student.school_class_name,
        studentCount = 0, // TODO: Query student count.
    )
    Student(
        id = student.id,
        name = student.name,
        orderInClass = student.order_in_class,
        surname = student.surname,
        email = student.email,
        phone = student.phone,
        schoolClass = schoolClass,
        grades = emptyList(), // TODO: Query grades.
    )
}

internal fun toExternal(students: List<GetStudentsBySchoolClassId>): List<BasicStudent> =
    students.map { student ->
        BasicStudent(
            id = student.id,
            classId = student.school_class_id,
            orderInClass = student.order_in_class,
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )
    }
