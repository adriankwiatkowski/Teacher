package com.example.teacherapp.feature.student.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicStudentNote
import com.example.teacherapp.core.model.data.StudentNote

internal class StudentNotePreviewParameterProvider : PreviewParameterProvider<StudentNote> {
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

internal class BasicStudentNotesPreviewParameterProvider :
    PreviewParameterProvider<List<BasicStudentNote>> {
    override val values: Sequence<List<BasicStudentNote>> = sequenceOf(
        listOf(
            BasicStudentNote(
                id = 1L,
                studentId = 1L,
                title = "Przeszkadzanie na zajęciach",
                isNegative = true,
            )
        )
    )
}