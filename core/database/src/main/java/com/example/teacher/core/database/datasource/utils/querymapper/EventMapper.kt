package com.example.teacher.core.database.datasource.utils.querymapper

import com.example.teacher.core.database.generated.queries.event.GetEventById
import com.example.teacher.core.database.generated.queries.event.GetEvents
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.Lesson

internal fun toExternal(events: List<GetEvents>): List<Event> = events.map { event ->
    Event(
        id = event.id,
        lesson = if (event.lesson_id != null) Lesson(
            id = event.lesson_id,
            name = event.lesson_name!!,
            schoolClass = BasicSchoolClass(
                id = event.school_class_id!!,
                name = event.school_class_name!!,
                studentCount = event.student_count,
                lessonCount = event.lesson_count,
            ),
        ) else null,
        date = event.date,
        startTime = event.start_time,
        endTime = event.end_time,
        isValid = event.is_valid,
    )
}

internal fun toExternal(event: GetEventById?): Event? = run {
    if (event == null) {
        return@run null
    }

    Event(
        id = event.id,
        lesson = if (event.lesson_id != null) Lesson(
            id = event.lesson_id,
            name = event.lesson_name!!,
            schoolClass = BasicSchoolClass(
                id = event.school_class_id!!,
                name = event.school_class_name!!,
                studentCount = event.student_count,
                lessonCount = event.lesson_count,
            ),
        ) else null,
        date = event.date,
        startTime = event.start_time,
        endTime = event.end_time,
        isValid = event.is_valid,
    )
}