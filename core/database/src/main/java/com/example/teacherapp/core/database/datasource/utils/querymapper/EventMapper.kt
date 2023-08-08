package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.event.GetEventById
import com.example.teacherapp.core.database.generated.queries.event.GetEvents
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Event
import com.example.teacherapp.core.model.data.Lesson

internal fun toExternal(events: List<GetEvents>): List<Event> = events.map { event ->
    Event(
        id = event.id,
        lesson = Lesson(
            id = event.lesson_id,
            name = event.lesson_name,
            schoolClass = BasicSchoolClass(
                id = event.school_class_id,
                name = event.school_class_name,
                studentCount = event.student_count,
                lessonCount = event.lesson_count,
            ),
        ),
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
        lesson = Lesson(
            id = event.lesson_id,
            name = event.lesson_name,
            schoolClass = BasicSchoolClass(
                id = event.school_class_id,
                name = event.school_class_name,
                studentCount = event.student_count,
                lessonCount = event.lesson_count,
            ),
        ),
        date = event.date,
        startTime = event.start_time,
        endTime = event.end_time,
        isValid = event.is_valid,
    )
}