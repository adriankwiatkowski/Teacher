package com.example.teacherapp.feature.student.note.data

import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

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