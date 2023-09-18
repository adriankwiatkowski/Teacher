package com.example.teacher.core.database.querymapper

import com.example.teacher.core.model.data.Note
import com.example.teacher.core.model.data.NotePriority
import com.example.teacher.core.database.generated.model.Note as NoteDto

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