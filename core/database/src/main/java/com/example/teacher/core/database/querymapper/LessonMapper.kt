package com.example.teacher.core.database.querymapper

import com.example.teacher.core.database.generated.queries.lesson.GetLessonById
import com.example.teacher.core.database.generated.queries.lesson.GetLessons
import com.example.teacher.core.database.generated.queries.lesson.GetLessonsBySchoolClassId
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.LessonsBySchoolClass
import com.example.teacher.core.model.data.LessonsByYear
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term

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
            schoolYear = SchoolYear(
                id = lesson.school_year_id,
                name = lesson.school_year_name,
                firstTerm = Term(
                    id = lesson.term_first_id,
                    name = lesson.term_first_name,
                    startDate = lesson.term_first_start_date,
                    endDate = lesson.term_first_end_date,
                ),
                secondTerm = Term(
                    id = lesson.term_second_id,
                    name = lesson.term_second_name,
                    startDate = lesson.term_second_start_date,
                    endDate = lesson.term_second_end_date,
                ),
            ),
            studentCount = lesson.student_count,
            lessonCount = lesson.lesson_count,
        ),
    )
}

internal fun toExternalLessonsByYear(
    getLessons: List<GetLessons>
): List<LessonsByYear> = getLessons
    .groupBy { lesson -> lesson.school_year_id }
    .map { (_, lessons) ->
        val schoolYear = lessons.first().let { lesson ->
            SchoolYear(
                id = lesson.school_year_id,
                name = lesson.school_year_name,
                firstTerm = Term(
                    id = lesson.term_first_id,
                    name = lesson.term_first_name,
                    startDate = lesson.term_first_start_date,
                    endDate = lesson.term_first_end_date,
                ),
                secondTerm = Term(
                    id = lesson.term_second_id,
                    name = lesson.term_second_name,
                    startDate = lesson.term_second_start_date,
                    endDate = lesson.term_second_end_date,
                ),
            )
        }
        val lessonBySchoolClass = lessons.groupBy { lesson -> lesson.school_class_id }

        LessonsByYear(
            year = schoolYear,
            lessonsBySchoolClass = lessonBySchoolClass.values.map { lessonsInSchoolClass ->
                val schoolClass = lessonsInSchoolClass.first().let { lesson ->
                    BasicSchoolClass(
                        id = lesson.school_class_id,
                        name = lesson.school_class_name,
                        schoolYear = schoolYear,
                        studentCount = lesson.student_count,
                        lessonCount = lesson.lesson_count,
                    )
                }

                LessonsBySchoolClass(
                    schoolClass = schoolClass,
                    lessons = lessonsInSchoolClass.map { lesson ->
                        Lesson(
                            id = lesson.id,
                            name = lesson.lesson_name,
                            schoolClass = schoolClass,
                        )
                    }
                )
            }
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
            schoolYear = SchoolYear(
                id = lesson.school_year_id,
                name = lesson.school_year_name,
                firstTerm = Term(
                    id = lesson.term_first_id,
                    name = lesson.term_first_name,
                    startDate = lesson.term_first_start_date,
                    endDate = lesson.term_first_end_date,
                ),
                secondTerm = Term(
                    id = lesson.term_second_id,
                    name = lesson.term_second_name,
                    startDate = lesson.term_second_start_date,
                    endDate = lesson.term_second_end_date,
                ),
            ),
            studentCount = lesson.student_count,
            lessonCount = lesson.lesson_count,
        ),
    )
}