package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lessonschedule.GetLessonSchedules
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.LessonSchedule

internal fun toExternal(
    lessonSchedules: List<GetLessonSchedules>
): List<LessonSchedule> = lessonSchedules.map { lessonSchedule ->
    LessonSchedule(
        id = lessonSchedule.id,
        lesson = Lesson(
            id = lessonSchedule.lesson_id,
            name = lessonSchedule.lesson_name,
            schoolClass = BasicSchoolClass(
                id = lessonSchedule.school_class_id,
                name = lessonSchedule.school_class_name,
                studentCount = lessonSchedule.student_count,
                lessonCount = lessonSchedule.lesson_count,
            ),
        ),
        date = lessonSchedule.date,
        startTime = lessonSchedule.start_time,
        endTime = lessonSchedule.end_time,
        isValid = lessonSchedule.is_valid,
    )
}