package com.example.teacherapp.feature.lesson.note.data

import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

internal data class NoteForm(
    val title: InputField<String>,
    val text: InputField<String>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = title.isValid && text.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}