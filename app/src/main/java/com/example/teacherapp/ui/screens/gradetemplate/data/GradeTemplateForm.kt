package com.example.teacherapp.ui.screens.gradetemplate.data

import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField

data class GradeTemplateForm(
    val name: InputField<String>,
    val description: InputField<String?>,
    val weight: InputField<String>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = name.isValid && description.isValid && weight.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}