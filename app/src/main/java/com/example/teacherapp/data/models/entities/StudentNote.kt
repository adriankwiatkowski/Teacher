package com.example.teacherapp.data.models.entities

data class StudentNote(
    val id: Long,
    val studentId: Long,
    val studentName: String,
    val studentSurname: String,
    val title: String,
    val description: String,
    val isNegative: Boolean,
) {
    val fullName = "$studentName $studentSurname"
}

data class BasicStudentNote(
    val id: Long,
    val studentId: Long,
    val title: String,
    val isNegative: Boolean,
)