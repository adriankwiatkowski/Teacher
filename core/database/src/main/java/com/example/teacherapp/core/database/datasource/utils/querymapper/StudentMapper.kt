package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.student.GetBasicStudentById
import com.example.teacherapp.core.database.generated.queries.student.GetStudentById
import com.example.teacherapp.core.database.generated.queries.student.GetStudentGradesById
import com.example.teacherapp.core.database.generated.queries.student.GetStudentsBySchoolClassId
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.core.model.data.StudentGrade
import com.example.teacherapp.core.model.data.StudentGradesByLesson
import java.math.BigDecimal

internal fun toExternal(student: GetBasicStudentById?): BasicStudent? = run {
    if (student == null) {
        return@run null
    }

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
    )
}

internal fun toExternal(
    students: List<GetStudentsBySchoolClassId>
): List<BasicStudent> = students.map { student ->
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

internal fun toExternalStudentGrades(
    studentGrades: List<GetStudentGradesById>
): List<StudentGradesByLesson> = studentGrades
    .groupBy { grade -> grade.lesson_id }
    .mapNotNull { (lessonId, grades) ->
        val firstGrade = grades.firstOrNull() ?: return@mapNotNull null
        val average = if (grades.isNotEmpty()) {
            grades.sumOf { grade -> grade.grade }.divide(BigDecimal(grades.size))
        } else {
            BigDecimal("0.00")
        }

        StudentGradesByLesson(
            studentId = firstGrade.student_id,
            lessonId = lessonId,
            lessonName = firstGrade.lesson_name,
            average = average,
            gradesByLessonId = grades.map { grade ->
                StudentGrade(
                    studentId = grade.student_id,
                    lessonId = grade.lesson_id,
                    gradeTemplateId = grade.grade_template_id,
                    gradeName = grade.grade_template_name,
                    gradeId = grade.grade_id,
                    grade = grade.grade,
                    weight = grade.grade_template_weight,
                    date = grade.grade_template_date,
                )
            }
        )
    }