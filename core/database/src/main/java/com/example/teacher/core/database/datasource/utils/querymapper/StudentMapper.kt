package com.example.teacher.core.database.datasource.utils.querymapper

import com.example.teacher.core.database.generated.queries.student.GetBasicStudentById
import com.example.teacher.core.database.generated.queries.student.GetStudentById
import com.example.teacher.core.database.generated.queries.student.GetStudentGradesById
import com.example.teacher.core.database.generated.queries.student.GetStudentsBySchoolClassId
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGrade
import com.example.teacher.core.model.data.StudentGradesByLesson
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
        studentCount = student.student_count,
        lessonCount = student.lesson_count,
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
    .map { (lessonId, grades) ->
        lessonId to grades.groupBy { grade -> grade.grade_is_first_term }
    }
    .map { (lessonId, gradesByTerm) ->
        val firstTermGrades = gradesByTerm[true] ?: emptyList()
        val firstTermAverage = calculateAverage(firstTermGrades)

        val secondTermGrades = gradesByTerm[false] ?: emptyList()
        val secondTermAverage = calculateAverage(secondTermGrades)

        val firstGrade = firstTermGrades.firstOrNull() ?: secondTermGrades.first()

        StudentGradesByLesson(
            studentId = firstGrade.student_id,
            lessonId = lessonId,
            lessonName = firstGrade.lesson_name,
            firstTermGrades = firstTermGrades.mapNotNull { it.toStudentGrades() },
            firstTermAverage = firstTermAverage,
            secondTermGrades = secondTermGrades.mapNotNull { it.toStudentGrades() },
            secondTermAverage = secondTermAverage,
        )
    }

private fun calculateAverage(grades: List<GetStudentGradesById>): BigDecimal? {
    val studentGrades = grades.filter { grade -> grade.grade != null }
    return if (studentGrades.isNotEmpty()) {
        val average = studentGrades.sumOf { grade -> grade.grade!! }
            .divide(BigDecimal.valueOf(studentGrades.size.toLong()))
        average
    } else {
        null
    }
}

private fun GetStudentGradesById.toStudentGrades(): StudentGrade? {
    if (grade_id == null || grade == null) {
        return null
    }

    return StudentGrade(
        studentId = student_id,
        lessonId = lesson_id,
        isFirstTerm = grade_is_first_term,
        gradeTemplateId = grade_template_id,
        gradeName = grade_template_name,
        gradeId = grade_id,
        grade = grade,
        weight = grade_template_weight,
        date = grade_template_date,
    )
}