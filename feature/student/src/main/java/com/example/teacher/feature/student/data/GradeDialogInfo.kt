package com.example.teacher.feature.student.data

import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGrade
import com.example.teacher.core.model.data.StudentGradesByLesson

internal data class GradeDialogInfo(
    val student: Student,
    val gradeInfo: StudentGradesByLesson,
    val grade: StudentGrade,
)