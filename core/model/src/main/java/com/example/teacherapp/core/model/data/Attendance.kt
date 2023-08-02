package com.example.teacherapp.core.model.data

enum class Attendance(val text: String) {
    Absent("nb"),
    ExcusedAbsence("u"),
    Late("sp"),
    Exemption("zw"),
    Present("ob");

    companion object {
        fun of(attendance: String): Attendance =
            Attendance.entries.first { entry -> entry.text == attendance }
    }
}