package com.example.teacherapp.core.database.datasource.utils.querymappers

import com.example.teacherapp.core.database.generated.queries.student.GetStudentNoteById
import com.example.teacherapp.core.database.generated.queries.student.GetStudentNotesByStudentId
import com.example.teacherapp.core.model.data.BasicStudentNote
import com.example.teacherapp.core.model.data.StudentNote

internal object StudentNoteMapper {

    fun GetStudentNotesByStudentId.mapToBasicNote() = BasicStudentNote(
        id = id,
        studentId = student_id,
        title = title,
        isNegative = is_negative,
    )

    fun GetStudentNoteById?.mapToNote(): StudentNote? {
        if (this == null) {
            return null
        }

        return StudentNote(
            id = id,
            studentId = student_id,
            studentName = student_name,
            studentSurname = student_surname,
            title = title,
            description = description,
            isNegative = is_negative,
        )
    }
}