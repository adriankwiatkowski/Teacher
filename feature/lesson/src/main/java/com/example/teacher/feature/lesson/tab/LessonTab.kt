package com.example.teacher.feature.lesson.tab

import androidx.annotation.StringRes
import com.example.teacher.feature.lesson.R

internal enum class LessonTab(@StringRes val title: Int) {
    Grades(title = R.string.grades),
    Attendance(title = R.string.attendance),
    Activity(title = R.string.activity),
    Notes(title = R.string.notes),
}