package com.example.teacherapp.ui.screens.paramproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Lesson

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
    )
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