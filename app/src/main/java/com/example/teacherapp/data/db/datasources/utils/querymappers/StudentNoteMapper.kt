package com.example.teacherapp.data.db.datasources.utils.querymappers

import com.example.teacherapp.data.db.queries.student.GetStudentNoteById
import com.example.teacherapp.data.db.queries.student.GetStudentNotesByStudentId
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.data.models.entities.StudentNote

object StudentNoteMapper {

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