package com.example.teacher.feature.schoolclass.tab

import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.feature.schoolclass.R

internal enum class SchoolClassTab(val icon: TeacherIcon) {
    Detail(TeacherIcons.details(R.string.school_class_details)),
    Students(TeacherIcons.people(R.string.school_class_students)),
    Subjects(TeacherIcons.subject(R.string.school_class_subjects)),
}