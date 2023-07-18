package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lessonactivity.GetLessonActivitiesByLessonId
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.LessonActivity

internal fun toExternal(
    lessonActivities: List<GetLessonActivitiesByLessonId>
): List<LessonActivity> = lessonActivities.map { lessonActivity ->
    LessonActivity(
        id = lessonActivity.id,
        lesson = BasicLesson(
            id = lessonActivity.lesson_id,
            name = lessonActivity.lesson_name,
            schoolClassId = lessonActivity.school_class_id,
        ),
        student = BasicStudent(
            id = lessonActivity.student_id,
            classId = lessonActivity.school_class_id,
            orderInClass = lessonActivity.student_order_in_class,
            name = lessonActivity.student_name,
            surname = lessonActivity.student_surname,
            email = lessonActivity.student_email,
            phone = lessonActivity.student_phone,
        ),
        sum = lessonActivity.sum,
    )
}