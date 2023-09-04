package com.example.teacher.core.database.datasource.utils.querymapper

import com.example.teacher.core.database.generated.queries.student.GetStudentNoteById
import com.example.teacher.core.database.generated.queries.student.GetStudentNotesByStudentId
import com.example.teacher.core.model.data.BasicStudentNote
import com.example.teacher.core.model.data.StudentNote

internal fun toExternal(notes: List<GetStudentNotesByStudentId>): List<BasicStudentNote> =
    notes.map { note ->
        BasicStudentNote(
            id = note.id,
            studentId = note.student_id,
            title = note.title,
            isNegative = note.is_negative,
        )
    }

internal fun toExternal(note: GetStudentNoteById?): StudentNote? = run {
    if (note == null) {
        return@run null
    }

    StudentNote(
        id = note.id,
        studentId = note.student_id,
        studentName = note.student_name,
        studentSurname = note.student_surname,
        title = note.title,
        description = note.description,
        isNegative = note.is_negative,
    )
}