package com.example.teacherapp.feature.note.data

import com.example.teacherapp.core.model.data.NotePriority
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

internal data class NoteForm(
    val title: InputField<String>,
    val text: InputField<String>,
    val priority: NotePriority,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = title.isValid && text.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}