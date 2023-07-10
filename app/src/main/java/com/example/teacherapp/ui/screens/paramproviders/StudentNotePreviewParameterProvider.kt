package com.example.teacherapp.ui.screens.paramproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.data.models.entities.StudentNote

class StudentNotePreviewParameterProvider : PreviewParameterProvider<StudentNote> {
    override val values: Sequence<StudentNote> = sequenceOf(
        StudentNote(
            id = 1L,
            studentId = 1L,
            studentName = "Jan",
            studentSurname = "Kowalski",
            title = "Przeszkadzanie na zajęciach",
            description = "Uczeń rozmawiał z kolegą z ławki",
            isNegative = true,
        )
    )
}

class BasicStudentNotePreviewParameterProvider : PreviewParameterProvider<BasicStudentNote> {
    override val values: Sequence<BasicStudentNote> = sequenceOf(
        BasicStudentNote(
            id = 1L,
            studentId = 1L,
            studentName = "Jan",
            studentSurname = "Kowalski",
            title = "Przeszkadzanie na zajęciach",
            isNegative = true,
        )
    )
}