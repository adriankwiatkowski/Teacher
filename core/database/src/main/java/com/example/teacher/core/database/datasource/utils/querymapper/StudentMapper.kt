package com.example.teacher.core.database.datasource.utils.querymapper

import com.example.teacher.core.common.utils.GradeUtils
import com.example.teacher.core.database.generated.queries.student.GetBasicStudentById
import com.example.teacher.core.database.generated.queries.student.GetStudentById
import com.example.teacher.core.database.generated.queries.student.GetStudentGradesById
import com.example.teacher.core.database.generated.queries.student.GetStudentsBySchoolClassId
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGrade
import com.example.teacher.core.model.data.StudentGradesByLesson
import com.example.teacher.core.model.data.Term
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
        schoolYear = SchoolYear(
            id = student.school_year_id,
            name = student.school_year_name,
            firstTerm = Term(
                id = student.term_first_id,
                name = student.term_first_name,
                startDate = student.term_first_start_date,
                endDate = student.term_first_end_date,
            ),
            secondTerm = Term(
                id = student.term_second_id,
                name = student.term_second_name,
                startDate = student.term_second_start_date,
                endDate = student.term_second_end_date,
            ),
        ),
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
            schoolClass = BasicSchoolClass(
                id = firstGrade.school_class_id,
                name = firstGrade.school_class_name,
                schoolYear = SchoolYear(
                    id = firstGrade.school_year_id,
                    name = firstGrade.school_year_name,
                    firstTerm = Term(
                        id = firstGrade.term_first_id,
                        name = firstGrade.term_first_name,
                        startDate = firstGrade.term_first_start_date,
                        endDate = firstGrade.term_first_end_date,
                    ),
                    secondTerm = Term(
                        id = firstGrade.term_second_id,
                        name = firstGrade.term_second_name,
                        startDate = firstGrade.term_second_start_date,
                        endDate = firstGrade.term_second_end_date,
                    ),
                ),
                studentCount = firstGrade.student_count,
                lessonCount = firstGrade.lesson_count,
            )
        )
    }

private fun calculateAverage(grades: List<GetStudentGradesById>): BigDecimal? {
    val studentGrades = grades.mapNotNull { it.grade }
    return GradeUtils.calculateAverage(studentGrades)
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