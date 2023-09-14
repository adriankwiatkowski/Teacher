package com.example.teacher.core.model.data

data class Student(
    val id: Long,
    val name: String,
    val registerNumber: Long,
    val surname: String,
    val email: String?,
    val phone: String?,
    val schoolClass: BasicSchoolClass,
) {
    val fullName = "$name $surname"
}

data class BasicStudent(
    val id: Long,
    val classId: Long,
    val registerNumber: Long,
    val name: String,
    val surname: String,
    val email: String?,
    val phone: String?,
) {
    val fullName = "$name $surname"
}