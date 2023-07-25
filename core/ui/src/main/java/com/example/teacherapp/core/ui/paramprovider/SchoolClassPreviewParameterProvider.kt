package com.example.teacherapp.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.SchoolClass

class SchoolClassPreviewParameterProvider : PreviewParameterProvider<SchoolClass> {
    override val values: Sequence<SchoolClass> = BasicSchoolClassPreviewParameterProvider()
        .values
        .map { basicSchoolClass ->
            SchoolClass(
                id = basicSchoolClass.id,
                name = basicSchoolClass.name,
                schoolYear = schoolYear,
                students = students,
                lessons = lessons,
            )
        }

    private val schoolYear = SchoolYearPreviewParameterProvider().values.first()
    private val lessons = BasicLessonPreviewParameterProvider().values.toList()
    private val students = BasicStudentPreviewParameterProvider().values.toList()
}

class BasicSchoolClassesPreviewParameterProvider :
    PreviewParameterProvider<List<BasicSchoolClass>> {
    override val values: Sequence<List<BasicSchoolClass>> = sequenceOf(
        BasicSchoolClassPreviewParameterProvider().values.toList(),
    )
}

class BasicSchoolClassPreviewParameterProvider : PreviewParameterProvider<BasicSchoolClass> {
    override val values: Sequence<BasicSchoolClass> = sequenceOf(
        BasicSchoolClass(1, "1A", 10, 10),
        BasicSchoolClass(2, "1B", 11, 11),
        BasicSchoolClass(3, "1C", 12, 12),
        BasicSchoolClass(4, "2A", 13, 13),
        BasicSchoolClass(5, "2B", 14, 14),
        BasicSchoolClass(6, "2C", 15, 15),
        BasicSchoolClass(7, "3A", 16, 16),
        BasicSchoolClass(8, "3B", 17, 17),
        BasicSchoolClass(9, "3C", 18, 18),
    )
}