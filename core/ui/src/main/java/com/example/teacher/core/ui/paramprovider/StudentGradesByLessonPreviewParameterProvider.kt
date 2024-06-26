package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.StudentGrade
import com.example.teacher.core.model.data.StudentGradeInfo
import com.example.teacher.core.model.data.StudentGradesByLesson
import java.math.BigDecimal

class StudentGradesByLessonPreviewParameterProvider :
    PreviewParameterProvider<List<StudentGradesByLesson>> {

    override val values: Sequence<List<StudentGradesByLesson>> = sequenceOf(
        listOf(
            StudentGradesByLesson(
                studentId = 1L,
                lessonId = 1L,
                lessonName = "Matematyka",
                firstTermAverage = BigDecimal("3.00"),
                firstTermGrades = listOf(
                    StudentGradeInfo(
                        studentId = 1L,
                        lessonId = 1L,
                        isFirstTerm = true,
                        gradeTemplateId = 1L,
                        gradeName = "Dodawanie",
                        grade = StudentGrade(
                            gradeId = 1L,
                            grade = BigDecimal("3.00"),
                        ),
                        weight = 3,
                        date = TimeUtils.currentDate(),
                    ),
                    StudentGradeInfo(
                        studentId = 1L,
                        lessonId = 1L,
                        isFirstTerm = true,
                        gradeTemplateId = 2L,
                        gradeName = "Odejmowanie",
                        grade = StudentGrade(
                            gradeId = 2L,
                            grade = BigDecimal("2.00"),
                        ),
                        weight = 3,
                        date = TimeUtils.currentDate(),
                    ),
                    StudentGradeInfo(
                        studentId = 1L,
                        lessonId = 1L,
                        isFirstTerm = true,
                        gradeTemplateId = 3L,
                        gradeName = "Mnożenie",
                        grade = null,
                        weight = 3,
                        date = TimeUtils.currentDate(),
                    ),
                ),
                secondTermAverage = null,
                secondTermGrades = emptyList(),
                schoolClass = BasicSchoolClassPreviewParameterProvider().values.first(),
            ),
        )
    )
}