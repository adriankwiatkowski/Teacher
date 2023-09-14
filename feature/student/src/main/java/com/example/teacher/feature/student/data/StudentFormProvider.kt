package com.example.teacher.feature.student.data

import androidx.core.text.trimmedLength
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.feature.student.R

internal object StudentFormProvider {

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
            supportingText = R.string.student_email_supporting_text,
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
            supportingText = R.string.student_phone_supporting_text,
            counter = trimmedLength to charCount,
            isError = trimmedLength != 0 && trimmedLength != charCount,
            isEdited = isEdited,
            isRequired = false,
        )
    }

    fun validateRegisterNumber(
        registerNumber: String?,
        usedRegisterNumbers: List<Long>?,
        isEdited: Boolean = true,
    ): InputField<String?> {
        var supportingText = R.string.student_register_number_supporting_text

        val isError = when {
            // Register numbers not loaded yet, show as error.
            usedRegisterNumbers == null -> true

            // Empty register number is allowed.
            registerNumber.isNullOrBlank() -> false

            // Check if register number is in fact number and is not used by other student.
            else -> registerNumber.toLongOrNull()?.let { number ->
                // Don't allow negative register number.
                if (number < 0) {
                    return@let true
                }

                val isNotUniqueNumber = number in usedRegisterNumbers
                if (isNotUniqueNumber) {
                    supportingText = R.string.student_register_number_supporting_text_error
                }

                // Allow swapping register numbers.
                false
            } ?: true
        }

        return InputField(
            registerNumber,
            supportingText = supportingText,
            isError = isError,
            isEdited = isEdited,
            isRequired = false,
        )
    }

    fun createDefaultForm(
        usedRegisterNumbers: List<Long>?,
        id: Long? = null,
        name: String = "",
        surname: String = "",
        email: String? = null,
        phone: String? = null,
        registerNumber: String? = null,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): StudentForm {
        return StudentForm(
            id = id,
            name = validateName(name, isEdited = isEdited),
            surname = validateSurname(surname, isEdited = isEdited),
            email = validateEmail(email, isEdited = isEdited),
            phone = validatePhone(phone, isEdited = isEdited),
            registerNumber = validateRegisterNumber(
                registerNumber,
                usedRegisterNumbers,
                isEdited = isEdited,
            ),
            status = status,
        )
    }
}