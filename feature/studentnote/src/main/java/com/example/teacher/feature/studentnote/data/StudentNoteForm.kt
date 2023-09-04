package com.example.teacher.feature.studentnote.data

import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal data class StudentNoteForm(
    val title: InputField<String>,
    val description: InputField<String?>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = title.isValid && description.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}