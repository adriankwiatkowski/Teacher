package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lesson.GetLessonById
import com.example.teacherapp.core.database.generated.queries.lesson.GetLessons
import com.example.teacherapp.core.database.generated.queries.lesson.GetLessonsBySchoolClassId
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Lesson

internal fun toExternal(
    lessons: List<GetLessonsBySchoolClassId>
): List<BasicLesson> = lessons.map { lesson ->
    BasicLesson(
        id = lesson.id,
        name = lesson.name,
        schoolClassId = lesson.school_class_id,
    )
}

internal fun toExternalLessons(
    lessons: List<GetLessons>
): List<Lesson> = lessons.map { lesson ->
    Lesson(
        id = lesson.id,
        name = lesson.lesson_name,
        schoolClass = BasicSchoolClass(
            id = lesson.school_class_id,
            name = lesson.school_class_name,
            studentCount = lesson.student_count,
            lessonCount = lesson.lesson_count,
        ),
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
            studentCount = lesson.student_count,
            lessonCount = lesson.lesson_count,
        ),
    )
}