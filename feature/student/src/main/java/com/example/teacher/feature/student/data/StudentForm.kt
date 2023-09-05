package com.example.teacher.feature.student.data

import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal data class StudentForm(
    val id: Long?,
    val name: InputField<String>,
    val surname: InputField<String>,
    val email: InputField<String?>,
    val phone: InputField<String?>,
    val status: FormStatus,
) {
    private val isValid: Boolean
        get() = name.isValid && surname.isValid && email.isValid && phone.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}