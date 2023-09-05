package com.example.teacher.feature.lesson.tab

import androidx.annotation.StringRes
import com.example.teacher.feature.lesson.R

internal enum class LessonTab(@StringRes val title: Int) {
    Grades(title = R.string.lesson_grades),
    Attendance(title = R.string.lesson_attendance),
    Activity(title = R.string.lesson_activity),
    Notes(title = R.string.lesson_notes),
}