package com.example.teacher.core.model.data

data class LessonActivity(
    val id: Long?,
    val lesson: BasicLesson,
    val student: BasicStudent,
    val sum: Long?,
    val isFirstTerm: Boolean,
)