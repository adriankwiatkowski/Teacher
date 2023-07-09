package com.example.teacherapp.data.db.datasources.utils.querymappers

import com.example.teacherapp.data.models.entities.BasicLesson
import com.example.teacherapp.data.models.entities.BasicSchoolClass
import com.example.teacherapp.data.models.entities.Lesson

object LessonMapper {

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