package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.Student

class StudentPreviewParameterProvider : PreviewParameterProvider<Student> {
    override val values: Sequence<Student> = BasicStudentPreviewParameterProvider()
        .values
        .map { basicStudent ->
            Student(
                id = basicStudent.id,
                name = basicStudent.name,
                registerNumber = basicStudent.registerNumber,
                surname = basicStudent.surname,
                email = basicStudent.email,
                phone = basicStudent.phone,
                schoolClass = basicSchoolClass,
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
            schoolClassId = index.toLong(),
            registerNumber = index + 1L,
            name = name,
            surname = surname,
            email = if (index % 2 == 0) "$name.$surname@email.com" else null,
            phone = if (index % 2 == 0) "12312312${index % 10}" else null,
        )
    }
}