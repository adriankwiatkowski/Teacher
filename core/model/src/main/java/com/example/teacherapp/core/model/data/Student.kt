package com.example.teacherapp.core.model.data

data class Student(
    val id: Long,
    val name: String,
    val orderInClass: Long,
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
    val orderInClass: Long,
    val name: String,
    val surname: String,
    val email: String?,
    val phone: String?,
) {
    val fullName = "$name $surname"
}