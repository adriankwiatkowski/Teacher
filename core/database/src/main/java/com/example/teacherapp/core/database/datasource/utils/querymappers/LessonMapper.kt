package com.example.teacherapp.core.database.datasource.utils.querymappers

import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.Lesson

internal object LessonMapper {

    fun mapBasicLesson(
        id: Long,
        name: String,
        schoolClassId: Long,
    ): BasicLesson {
        return BasicLesson(
            id = id,
            name = name,
            schoolClassId = schoolClassId,
        )
    }

    fun mapLesson(
        id: Long,
        lessonName: String,
        schoolClassId: Long,
        schoolClassName: String
    ): Lesson {
        return Lesson(
            id = id,
            name = lessonName,
            basicSchoolClass = BasicSchoolClass(
                id = schoolClassId,
                name = schoolClassName,
                studentCount = 0, // TODO: Query student count.
            )
        )
    }
}