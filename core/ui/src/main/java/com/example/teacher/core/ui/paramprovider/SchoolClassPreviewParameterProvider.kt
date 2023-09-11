package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.core.model.data.SchoolClassesByYear

class SchoolClassPreviewParameterProvider : PreviewParameterProvider<SchoolClass> {

    override val values: Sequence<SchoolClass> = BasicSchoolClassPreviewParameterProvider()
        .values
        .map { basicSchoolClass ->
            SchoolClass(
                id = basicSchoolClass.id,
                name = basicSchoolClass.name,
                schoolYear = basicSchoolClass.schoolYear,
                students = students,
                lessons = lessons,
            )
        }

    private val lessons = BasicLessonPreviewParameterProvider().values.toList()
    private val students = BasicStudentPreviewParameterProvider().values.toList()
}

class SchoolClassesByYearPreviewParameterProvider :
    PreviewParameterProvider<List<SchoolClassesByYear>> {

    private val schoolYears = SchoolYearPreviewParameterProvider().values.toList()
    private val schoolClasses = BasicSchoolClassPreviewParameterProvider().values.toList()

    override val values: Sequence<List<SchoolClassesByYear>> = sequenceOf(
        schoolYears
            .mapIndexed { index, schoolYear ->
                val schoolClasses = schoolClasses.map { schoolClass ->
                    schoolClass.copy(
                        id = index * schoolClasses.size + schoolClass.id,
                        schoolYear = schoolYear,
                    )
                }

                SchoolClassesByYear(year = schoolYear, schoolClasses = schoolClasses)
            }
    )
}

class BasicSchoolClassPreviewParameterProvider : PreviewParameterProvider<BasicSchoolClass> {

    private val schoolYear = SchoolYearPreviewParameterProvider().values.first()

    override val values: Sequence<BasicSchoolClass> = sequenceOf(
        BasicSchoolClass(1, "1A", schoolYear, 10, 10),
        BasicSchoolClass(2, "1B", schoolYear, 11, 11),
        BasicSchoolClass(3, "2A", schoolYear, 13, 13),
        BasicSchoolClass(4, "2B", schoolYear, 14, 14),
        BasicSchoolClass(5, "3A", schoolYear, 16, 16),
        BasicSchoolClass(6, "3B", schoolYear, 17, 17),
    )
}