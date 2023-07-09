package com.example.teacherapp.data.models.entities

data class Student(
    val id: Long,
    val name: String,
    val orderInClass: Long,
    val surname: String,
    val email: String?,
    val phone: String?,
    val schoolClass: BasicSchoolClass,
    val grades: List<Grade>,
)

data class BasicStudent(
    val id: Long,
    val classId: Long,
    val orderInClass: Long,
    val name: String,
    val surname: String,
    val email: String?,
    val phone: String?,
)

fun Student.toBasicStudent() = BasicStudent(
    id = id,
    classId = schoolClass.id,
    orderInClass = orderInClass,
    name = name,
    surname = surname,
    email = email,
    phone = phone,
)