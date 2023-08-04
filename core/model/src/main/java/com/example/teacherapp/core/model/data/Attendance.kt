package com.example.teacherapp.core.model.data

enum class Attendance(val text: String) {
    Present("ob"),
    Late("sp"),
    Absent("nb"),
    ExcusedAbsence("u"),
    Exemption("zw");

    companion object {
        fun of(attendance: String): Attendance =
            Attendance.entries.first { entry -> entry.text == attendance }
    }
}