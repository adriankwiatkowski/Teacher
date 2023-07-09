package com.example.teacherapp.ui.screens.paramproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.data.models.entities.BasicStudent
import com.example.teacherapp.data.models.entities.Grade
import com.example.teacherapp.data.models.entities.Lesson
import com.example.teacherapp.data.models.entities.Student
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
                        studentId = basicStudent.id,
                        lesson = Lesson(1L, "Matematyka", basicSchoolClass),
                        name = "Sprawdzian",
                        description = "Trygonometria",
                        grade = BigDecimal.valueOf(4L),
                        weight = 5,
                        date = LocalDate.now(),
                    )
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