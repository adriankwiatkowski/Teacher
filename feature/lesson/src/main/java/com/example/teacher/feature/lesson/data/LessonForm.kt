package com.example.teacher.feature.lesson.data

import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal data class LessonForm(
    val id: Long?,
    val name: InputField<String>,
    val status: FormStatus,
) {
    private val isValid: Boolean
        get() = name.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}