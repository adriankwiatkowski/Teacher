package com.example.teacherapp.ui.screens.student.data

import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField

data class StudentForm(
    val id: Long?,
    val name: InputField<String>,
    val surname: InputField<String>,
    val email: InputField<String?>,
    val phone: InputField<String?>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = name.isValid && surname.isValid && email.isValid && phone.isValid
}