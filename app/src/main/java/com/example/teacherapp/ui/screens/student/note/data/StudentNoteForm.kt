package com.example.teacherapp.ui.screens.student.note.data

import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField

data class StudentNoteForm(
    val title: InputField<String>,
    val description: InputField<String?>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = title.isValid && description.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}