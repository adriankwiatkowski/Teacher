package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonsBySchoolClass
import com.example.teacher.core.model.data.LessonsByYear

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

    private val basicSchoolClass = BasicSchoolClassPreviewParameterProvider().values.first()
}

class LessonsByYearPreviewParameterProvider : PreviewParameterProvider<List<LessonsByYear>> {

    private val schoolClasses = BasicSchoolClassPreviewParameterProvider().values
    private val lessons = LessonPreviewParameterProvider().values.toList()

    override val values: Sequence<List<LessonsByYear>> = sequenceOf(
        schoolClasses
            .groupBy { it.schoolYear.id }
            .map { (_, schoolClasses) ->
                val schoolYear = schoolClasses.first().schoolYear
                val newSchoolClasses = schoolClasses.map { schoolClass ->
                    schoolClass.copy(id = ++schoolClassId)
                }

                LessonsByYear(
                    year = schoolYear,
                    lessonsBySchoolClass = newSchoolClasses.map { schoolClass ->
                        LessonsBySchoolClass(
                            schoolClass = schoolClass,
                            lessons = lessons.map { lesson ->
                                lesson.copy(id = ++lessonId, schoolClass = schoolClass)
                            },
                        )
                    }
                )
            }
    )

    private var schoolClassId = 0L
    private var lessonId = 0L
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