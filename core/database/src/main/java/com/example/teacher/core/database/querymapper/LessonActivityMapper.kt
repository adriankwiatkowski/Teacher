package com.example.teacher.core.database.querymapper

import com.example.teacher.core.database.generated.queries.lessonactivity.GetLessonActivitiesByLessonId
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.LessonActivity

internal fun toExternal(
    lessonActivities: List<GetLessonActivitiesByLessonId>
): List<LessonActivity> = lessonActivities
    .groupBy { lessonActivity -> lessonActivity.student_id }
    .mapValues { (_, lessonActivitiesByStudent) ->
        when (lessonActivitiesByStudent.size) {
            1 -> {
                val lessonActivityRow = lessonActivitiesByStudent.first()
                val newLessonActivitiesForStudent = mutableListOf<LessonActivity>()

                run {
                    val lessonActivity = if (lessonActivityRow.is_first_term == null) {
                        lessonActivityRow.toDefaultLessonActivity()
                    } else {
                        lessonActivityRow.toLessonActivity()
                    }
                    newLessonActivitiesForStudent.add(lessonActivity)
                }

                // Add lesson activity for other term.
                run {
                    val existingLessonActivity = newLessonActivitiesForStudent.first()

                    newLessonActivitiesForStudent.add(
                        existingLessonActivity.copy(
                            id = null,
                            sum = null,
                            isFirstTerm = !existingLessonActivity.isFirstTerm,
                        )
                    )
                }

                newLessonActivitiesForStudent
            }

            2 -> lessonActivitiesByStudent.map { lessonActivity ->
                lessonActivity.toLessonActivity()
            }

            else -> throw IllegalStateException()
        }
    }
    .values
    .flatten()

private fun GetLessonActivitiesByLessonId.toDefaultLessonActivity(): LessonActivity =
    LessonActivity(
        id = null,
        lesson = BasicLesson(
            id = lesson_id,
            name = lesson_name,
            schoolClassId = school_class_id,
        ),
        student = BasicStudent(
            id = student_id,
            classId = school_class_id,
            registerNumber = student_register_number,
            name = student_name,
            surname = student_surname,
            email = student_email,
            phone = student_phone,
        ),
        sum = null,
        isFirstTerm = true,
    )

private fun GetLessonActivitiesByLessonId.toLessonActivity(): LessonActivity = LessonActivity(
    id = id,
    lesson = BasicLesson(
        id = lesson_id,
        name = lesson_name,
        schoolClassId = school_class_id,
    ),
    student = BasicStudent(
        id = student_id,
        classId = school_class_id,
        registerNumber = student_register_number,
        name = student_name,
        surname = student_surname,
        email = student_email,
        phone = student_phone,
    ),
    sum = sum,
    isFirstTerm = is_first_term!!,
)