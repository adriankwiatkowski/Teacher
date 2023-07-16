package com.example.teacherapp.ui.screens.paramproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Grade
import com.example.teacherapp.core.model.data.GradeTemplate
import com.example.teacherapp.core.model.data.Student
import java.math.BigDecimal
import java.time.LocalDate

class StudentPreviewParameterProvider : PreviewParameterProvider<Student> {
    override val values: Sequence<Student> = BasicStudentPreviewParameterProvider()
        .values
        .map { basicStudent ->
            Student(
                id = basicStudent.id,
                name = basicStudent.name,
                orderInClass = basicStudent.orderInClass,
                surname = basicStudent.surname,
                email = basicStudent.email,
                phone = basicStudent.phone,
                schoolClass = basicSchoolClass,
                grades = listOf(
                    Grade(
                        id = 1L,
                        grade = BigDecimal.valueOf(4L),
                        date = LocalDate.now(),
                        studentId = basicStudent.id,
                        studentFullName = "${basicStudent.name} ${basicStudent.surname}",
                        lessonId = 1L,
                        lessonName = "Matematyka",
                        schoolClassId = basicSchoolClass.id,
                        schoolClassName = basicSchoolClass.name,
                        gradeTemplate = GradeTemplate(
                            id = 1L,
                            name = "Sprawdzian",
                            description = "Trygonometria",
                            weight = 5,
                            lessonId = 1L,
                        ),
                    ),
                ),
            )
        }

    private val basicSchoolClass = BasicSchoolClassPreviewParameterProvider().values.first()
}

class BasicStudentsPreviewParameterProvider : PreviewParameterProvider<List<BasicStudent>> {
    override val values: Sequence<List<BasicStudent>> = sequenceOf(
        BasicStudentPreviewParameterProvider().values.toList(),
    )
}

class BasicStudentPreviewParameterProvider : PreviewParameterProvider<BasicStudent> {
    override val values: Sequence<BasicStudent> = sequenceOf(
        "Jan" to "Kowalski",
        "Małgosiwa" to "Kowalska",
        "Jan" to "Nowak",
        "Małgosiwa" to "Nowak",
    ).mapIndexed { index, (name, surname) ->
        BasicStudent(
            id = index + 1L,
            classId = index + 1L,
            orderInClass = index + 1L,
            name = name,
            surname = surname,
            email = null,
            phone = null,
        )
    }
}