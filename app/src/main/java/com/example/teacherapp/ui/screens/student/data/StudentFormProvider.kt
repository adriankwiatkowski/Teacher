package com.example.teacherapp.ui.screens.student.data

import androidx.core.text.trimmedLength
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField

object StudentFormProvider {

    fun validateName(name: String, isEdited: Boolean = true): InputField<String> {
        val trimmedLength = name.trimmedLength()
        val charCountLimit = 60
        return InputField(
            name,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 1..charCountLimit,
            isEdited = isEdited,
        )
    }

    fun validateSurname(surname: String, isEdited: Boolean = true): InputField<String> {
        val trimmedLength = surname.trimmedLength()
        val charCountLimit = 60
        return InputField(
            surname,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 1..charCountLimit,
            isEdited = isEdited,
        )
    }

    fun validateEmail(email: String?, isEdited: Boolean = true): InputField<String?> {
        val trimmedLength = email?.trimmedLength() ?: 0
        val charCountLimit = 60
        return InputField(
            email,
            supportingText = "Może być pusty",
            counter = trimmedLength to charCountLimit,
            isError = false,
            isEdited = isEdited,
            isRequired = false,
        )
    }

    fun validatePhone(phone: String?, isEdited: Boolean = true): InputField<String?> {
        val trimmedLength = phone?.trimmedLength() ?: 0
        val charCount = 9
        return InputField(
            phone,
            supportingText = "Telefon musi zawierać $charCount cyfr (może być pusty)",
            counter = trimmedLength to charCount,
            isError = trimmedLength != 0 && trimmedLength != charCount,
            isEdited = isEdited,
            isRequired = false,
        )
    }

    fun createDefaultForm(
        id: Long? = null,
        name: String = "",
        surname: String = "",
        email: String? = null,
        phone: String? = null,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): StudentForm {
        return StudentForm(
            id = id,
            name = validateName(name, isEdited = isEdited),
            surname = validateSurname(surname, isEdited = isEdited),
            email = validateEmail(email, isEdited = isEdited),
            phone = validatePhone(phone, isEdited = isEdited),
            status = status,
        )
    }
}