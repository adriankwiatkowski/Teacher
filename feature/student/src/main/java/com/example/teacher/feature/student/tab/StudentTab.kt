package com.example.teacher.feature.student.tab

import androidx.annotation.StringRes
import com.example.teacher.feature.student.R

enum class StudentTab(@StringRes val title: Int) {
    Detail(title = R.string.details),
    Grades(title = R.string.grades),
    Notes(title = R.string.notes),
}