package com.example.teacher.feature.student.tab

import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.feature.student.R

internal enum class StudentTab(val icon: TeacherIcon) {
    Detail(TeacherIcons.details(R.string.student_details)),
    Grades(TeacherIcons.grades(R.string.student_grades)),
    Notes(TeacherIcons.notes(R.string.student_notes)),
}