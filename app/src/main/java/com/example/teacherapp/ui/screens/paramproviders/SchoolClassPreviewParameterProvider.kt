package com.example.teacherapp.ui.screens.paramproviders

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.data.models.entities.BasicSchoolClass
import com.example.teacherapp.data.models.entities.ExpandableBasicSchoolClasses
import com.example.teacherapp.data.models.entities.SchoolClass
import com.example.teacherapp.data.models.entities.SchoolYear

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

class ExpandableBasicSchoolClassesMapPreviewParameterProvider :
    PreviewParameterProvider<Map<SchoolYear, ExpandableBasicSchoolClasses>> {
    override val values: Sequence<Map<SchoolYear, ExpandableBasicSchoolClasses>> = sequenceOf(
        SchoolClassPreviewParameterProvider().values
            .groupBy { it.schoolYear }
            .mapValues { (_, schoolClasses) ->
                ExpandableBasicSchoolClasses(
                    expanded = mutableStateOf(false),
                    schoolClasses = schoolClasses.map { schoolClass ->
                        BasicSchoolClass(
                            id = schoolClass.id,
                            name = schoolClass.name,
                            studentCount = schoolClass.students.size,
                        )
                    }
                )
            }
    )
}

class BasicSchoolClassesPreviewParameterProvider :
    PreviewParameterProvider<List<BasicSchoolClass>> {
    override val values: Sequence<List<BasicSchoolClass>> = sequenceOf(
        BasicSchoolClassPreviewParameterProvider().values.toList(),
    )
}

class BasicSchoolClassPreviewParameterProvider : PreviewParameterProvider<BasicSchoolClass> {
    override val values: Sequence<BasicSchoolClass> = sequenceOf(
        BasicSchoolClass(1, "1A", 10),
        BasicSchoolClass(2, "1B", 11),
        BasicSchoolClass(3, "1C", 12),
        BasicSchoolClass(4, "2A", 13),
        BasicSchoolClass(5, "2B", 14),
        BasicSchoolClass(6, "2C", 15),
        BasicSchoolClass(7, "3A", 16),
        BasicSchoolClass(8, "3B", 17),
        BasicSchoolClass(9, "3C", 18),
    )
}