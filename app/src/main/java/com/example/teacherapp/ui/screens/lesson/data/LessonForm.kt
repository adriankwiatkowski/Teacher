package com.example.teacherapp.ui.screens.lesson.data

import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField

data class LessonForm(
    val id: Long?,
    val name: InputField<String>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = name.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}