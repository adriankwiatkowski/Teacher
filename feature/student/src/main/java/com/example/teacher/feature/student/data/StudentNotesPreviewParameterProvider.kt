package com.example.teacher.feature.student.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.BasicStudentNotesPreviewParameterProvider

internal class StudentNotesPreviewParameterProvider :
    PreviewParameterProvider<StudentNotes> {
    override val values: Sequence<StudentNotes> = sequenceOf(emptyNotes) + studentNotesValues
        .map { studentNotes ->
            StudentNotes(
                neutralNotes = studentNotes.filter { !it.isNegative },
                negativeNotes = studentNotes.filter { it.isNegative },
            )
        }
}

private val studentNotesValues = BasicStudentNotesPreviewParameterProvider().values
private val emptyNotes = StudentNotes(neutralNotes = emptyList(), negativeNotes = emptyList())