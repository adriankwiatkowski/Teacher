package com.example.teacher.feature.student.data

import com.example.teacher.core.model.data.BasicStudentNote

internal data class StudentNotes(
    val neutralNotes: List<BasicStudentNote>,
    val negativeNotes: List<BasicStudentNote>,
)