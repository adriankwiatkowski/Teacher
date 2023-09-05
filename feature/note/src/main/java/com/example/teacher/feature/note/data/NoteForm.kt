package com.example.teacher.feature.note.data

import com.example.teacher.core.model.data.NotePriority
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal data class NoteForm(
    val title: InputField<String>,
    val text: InputField<String>,
    val priority: NotePriority,
    val status: FormStatus,
) {
    private val isValid: Boolean
        get() = title.isValid && text.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}