package com.example.teacher.core.model.data

data class Lesson(
    val id: Long,
    val name: String,
    val schoolClass: BasicSchoolClass,
)

data class BasicLesson(
    val id: Long,
    val name: String,
    val schoolClassId: Long,
)

data class LessonsByYear(
    val year: SchoolYear,
    val lessonsBySchoolClass: List<LessonsBySchoolClass>,
)

data class LessonsBySchoolClass(
    val schoolClass: BasicSchoolClass,
    val lessons: List<Lesson>,
)