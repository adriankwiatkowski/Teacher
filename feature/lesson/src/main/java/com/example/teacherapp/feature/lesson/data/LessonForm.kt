package com.example.teacherapp.feature.lesson.data

import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

internal data class LessonForm(
    val id: Long?,
    val name: InputField<String>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = name.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}