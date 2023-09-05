package com.example.teacher.feature.lesson.gradetemplate.data

import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal data class GradeTemplateForm(
    val name: InputField<String>,
    val description: InputField<String?>,
    val weight: InputField<String>,
    val isFirstTerm: Boolean,
    val status: FormStatus,
) {
    private val isValid: Boolean
        get() = name.isValid && description.isValid && weight.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}