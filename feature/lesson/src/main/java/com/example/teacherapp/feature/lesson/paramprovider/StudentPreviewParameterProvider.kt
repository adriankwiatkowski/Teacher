package com.example.teacherapp.feature.lesson.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicStudent

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