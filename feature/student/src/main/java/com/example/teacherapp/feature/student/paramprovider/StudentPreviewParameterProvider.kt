package com.example.teacherapp.feature.student.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Student

internal class StudentPreviewParameterProvider : PreviewParameterProvider<Student> {
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
            )
        }

    private val basicSchoolClass = BasicSchoolClassPreviewParameterProvider().values.first()
}

internal class BasicStudentsPreviewParameterProvider :
    PreviewParameterProvider<List<BasicStudent>> {
    override val values: Sequence<List<BasicStudent>> = sequenceOf(
        BasicStudentPreviewParameterProvider().values.toList(),
    )
}

internal class BasicStudentPreviewParameterProvider : PreviewParameterProvider<BasicStudent> {
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
            email = if (index % 2 == 0) "$name.$surname@email.com" else null,
            phone = if (index % 2 == 0) "12312312${index % 10}" else null,
        )
    }
}