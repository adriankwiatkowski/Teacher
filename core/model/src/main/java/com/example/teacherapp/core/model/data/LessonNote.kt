package com.example.teacherapp.core.model.data

data class LessonNote(
    val id: Long,
    val title: String,
    val text: String,
    val lesson: BasicLesson,
)

data class BasicLessonNote(
    val id: Long,
    val title: String,
    val text: String,
    val lessonId: Long,
)