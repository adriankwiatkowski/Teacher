package com.example.teacher.core.database.querymapper

import com.example.teacher.core.database.generated.queries.event.GetEventById
import com.example.teacher.core.database.generated.queries.event.GetEvents
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term

internal fun toExternal(events: List<GetEvents>): List<Event> = events.map { event ->
    Event(
        id = event.id,
        lesson = if (event.lesson_id != null) Lesson(
            id = event.lesson_id,
            name = event.lesson_name!!,
            schoolClass = BasicSchoolClass(
                id = event.school_class_id!!,
                name = event.school_class_name!!,
                schoolYear = SchoolYear(
                    id = event.school_year_id!!,
                    name = event.school_year_name!!,
                    firstTerm = Term(
                        id = event.term_first_id!!,
                        name = event.term_first_name!!,
                        startDate = event.term_first_start_date!!,
                        endDate = event.term_first_end_date!!,
                    ),
                    secondTerm = Term(
                        id = event.term_second_id!!,
                        name = event.term_second_name!!,
                        startDate = event.term_second_start_date!!,
                        endDate = event.term_second_end_date!!,
                    ),
                ),
                studentCount = event.student_count,
                lessonCount = event.lesson_count,
            ),
        ) else null,
        date = event.date,
        startTime = event.start_time,
        endTime = event.end_time,
        isCancelled = event.is_cancelled,
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
                schoolYear = SchoolYear(
                    id = event.school_year_id!!,
                    name = event.school_year_name!!,
                    firstTerm = Term(
                        id = event.term_first_id!!,
                        name = event.term_first_name!!,
                        startDate = event.term_first_start_date!!,
                        endDate = event.term_first_end_date!!,
                    ),
                    secondTerm = Term(
                        id = event.term_second_id!!,
                        name = event.term_second_name!!,
                        startDate = event.term_second_start_date!!,
                        endDate = event.term_second_end_date!!,
                    ),
                ),
                studentCount = event.student_count,
                lessonCount = event.lesson_count,
            ),
        ) else null,
        date = event.date,
        startTime = event.start_time,
        endTime = event.end_time,
        isCancelled = event.is_cancelled,
    )
}