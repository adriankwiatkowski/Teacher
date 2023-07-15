package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lesson.GetLessonById
import com.example.teacherapp.core.database.generated.queries.lesson.GetLessonsBySchoolClassId
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Lesson

internal fun toExternal(lessons: List<GetLessonsBySchoolClassId>): List<BasicLesson> =
    lessons.map { lesson ->
        BasicLesson(
            id = lesson.id,
            name = lesson.name,
            schoolClassId = lesson.school_class_id,
        )
    }

internal fun toExternal(lesson: GetLessonById?): Lesson? = run {
    if (lesson == null) {
        return@run null
    }

    Lesson(
        id = lesson.id,
        name = lesson.lesson_name,
        schoolClass = BasicSchoolClass(
            id = lesson.school_class_id,
            name = lesson.school_class_name,
            studentCount = 0, // TODO: Query student count.
        ),
    )
}