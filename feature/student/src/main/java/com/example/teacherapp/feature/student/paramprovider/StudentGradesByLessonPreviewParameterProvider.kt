package com.example.teacherapp.feature.student.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.StudentGrade
import com.example.teacherapp.core.model.data.StudentGradesByLesson
import java.math.BigDecimal
import java.time.LocalDate

internal class StudentGradesByLessonPreviewParameterProvider :
    PreviewParameterProvider<List<StudentGradesByLesson>> {

    override val values: Sequence<List<StudentGradesByLesson>> = sequenceOf(
        listOf(
            StudentGradesByLesson(
                studentId = 1L,
                lessonId = 1L,
                lessonName = "Matematyka",
                average = BigDecimal("3.00"),
                gradesByLessonId = listOf(
                    StudentGrade(
                        studentId = 1L,
                        lessonId = 1L,
                        gradeTemplateId = 1L,
                        gradeName = "Dodawanie",
                        gradeId = 1L,
                        grade = BigDecimal("3.00"),
                        weight = 3,
                        date = LocalDate.now(),
                    ),
                    StudentGrade(
                        studentId = 1L,
                        lessonId = 1L,
                        gradeTemplateId = 2L,
                        gradeName = "Odejmowanie",
                        gradeId = 2L,
                        grade = BigDecimal("2.00"),
                        weight = 3,
                        date = LocalDate.now(),
                    ),
                    StudentGrade(
                        studentId = 1L,
                        lessonId = 1L,
                        gradeTemplateId = 3L,
                        gradeName = "Mnożenie",
                        gradeId = 3L,
                        grade = BigDecimal("4.00"),
                        weight = 3,
                        date = LocalDate.now(),
                    ),
                ),
            ),
        )
    )
}