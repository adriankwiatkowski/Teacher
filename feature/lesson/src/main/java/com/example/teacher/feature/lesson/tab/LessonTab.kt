package com.example.teacher.feature.lesson.tab

import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.feature.lesson.R

internal enum class LessonTab(val icon: TeacherIcon) {
    Grades(TeacherIcons.grades(R.string.lesson_grades)),
    Attendance(TeacherIcons.attendance(R.string.lesson_attendance)),
    Activity(TeacherIcons.activity(R.string.lesson_activity)),
    Notes(TeacherIcons.notes(R.string.lesson_notes)),
}