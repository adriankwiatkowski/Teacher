package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lesson.GetLessonNoteById
import com.example.teacherapp.core.database.generated.queries.lesson.GetLessonNotesByLessonId
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.BasicLessonNote
import com.example.teacherapp.core.model.data.LessonNote

internal fun toExternal(
    notes: List<GetLessonNotesByLessonId>
): List<BasicLessonNote> = notes.map { note ->
    BasicLessonNote(
        id = note.id,
        title = note.title,
        text = note.text,
        lessonId = note.lesson_id,
    )
}

internal fun toExternal(note: GetLessonNoteById?): LessonNote? = run {
    if (note == null) {
        return@run null
    }

    LessonNote(
        id = note.id,
        title = note.title,
        text = note.text,
        lesson = BasicLesson(
            id = note.lesson_id,
            name = note.lesson_name,
            schoolClassId = note.school_class_id,
        ),
    )
}