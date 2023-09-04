package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonWithSchoolYear

class LessonsPreviewParameterProvider : PreviewParameterProvider<List<Lesson>> {
    override val values: Sequence<List<Lesson>> = sequenceOf(
        LessonPreviewParameterProvider()
            .values
            .toList(),
    )
}

class LessonPreviewParameterProvider : PreviewParameterProvider<Lesson> {
    override val values: Sequence<Lesson> = BasicLessonPreviewParameterProvider()
        .values
        .map { lesson ->
            Lesson(
                id = lesson.id,
                name = lesson.name,
                schoolClass = basicSchoolClass,
            )
        }

    private val basicSchoolClass = BasicSchoolClass(
        id = 1L,
        name = "3A",
        studentCount = 20,
        lessonCount = 4,
    )
}

class LessonWithSchoolYearPreviewParameterProvider :
    PreviewParameterProvider<LessonWithSchoolYear> {
    override val values: Sequence<LessonWithSchoolYear> = LessonPreviewParameterProvider()
        .values
        .map { lesson ->
            LessonWithSchoolYear(
                id = lesson.id,
                name = lesson.name,
                schoolClassId = lesson.schoolClass.id,
                schoolClassName = lesson.schoolClass.name,
                schoolYear = SchoolYearPreviewParameterProvider().values.first(),
            )
        }
}

class BasicLessonsPreviewParameterProvider : PreviewParameterProvider<List<BasicLesson>> {
    override val values: Sequence<List<BasicLesson>> = sequenceOf(
        BasicLessonPreviewParameterProvider()
            .values
            .toList(),
    )
}

class BasicLessonPreviewParameterProvider : PreviewParameterProvider<BasicLesson> {
    override val values: Sequence<BasicLesson> = sequenceOf(
        "Matematyka",
        "Geografia",
        "Polski",
        "Angielski",
        "Historia",
        "Fizyka",
    ).mapIndexed { i, name ->
        BasicLesson(
            id = i + 1L,
            name = name,
            schoolClassId = 1L,
        )
    }
}