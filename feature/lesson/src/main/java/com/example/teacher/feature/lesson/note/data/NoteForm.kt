package com.example.teacher.feature.lesson.note.data

import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal data class NoteForm(
    val title: InputField<String>,
    val text: InputField<String>,
    val status: FormStatus,
) {
    private val isValid: Boolean
        get() = title.isValid && text.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}