package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.model.data.Note
import com.example.teacherapp.core.model.data.NotePriority
import com.example.teacherapp.core.database.generated.model.Note as NoteDto

internal fun toExternal(notes: List<NoteDto>): List<Note> = notes.mapNotNull(::toExternal)

internal fun toExternal(note: NoteDto?): Note? = run {
    if (note == null) {
        return@run null
    }

    Note(
        id = note.id,
        title = note.title,
        text = note.text,
        priority = NotePriority.ofPriority(note.priority),
    )
}