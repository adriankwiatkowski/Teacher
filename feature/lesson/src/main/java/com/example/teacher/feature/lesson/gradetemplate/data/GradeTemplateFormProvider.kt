package com.example.teacher.feature.lesson.gradetemplate.data

import androidx.core.text.trimmedLength
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal object GradeTemplateFormProvider {

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

    fun validateDescription(description: String?, isEdited: Boolean = true): InputField<String?> {
        val trimmedLength = description?.trimmedLength() ?: 0
        val charCountLimit = 200
        return InputField(
            description,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 0..charCountLimit,
            isEdited = isEdited,
            isRequired = false,
        )
    }

    fun validateWeight(weight: String, isEdited: Boolean = true): InputField<String> {
        val isError = weight.trim().toIntOrNull()?.let { weightNumber ->
            weightNumber !in 1..6
        } ?: true
        val supportingText = if (isError) "Waga musi być liczbą od 1 do 6" else null

        return InputField(
            weight,
            supportingText = supportingText,
            counter = null,
            isError = isError,
            isEdited = isEdited,
        )
    }

    fun createDefaultForm(
        name: String = "",
        description: String? = null,
        weight: String = DefaultWeight,
        isFirstTerm: Boolean = true,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): GradeTemplateForm {
        return GradeTemplateForm(
            name = validateName(name, isEdited = isEdited),
            description = validateDescription(description, isEdited = isEdited),
            weight = validateWeight(weight, isEdited = isEdited),
            isFirstTerm = isFirstTerm,
            status = status,
        )
    }
}

private const val DefaultWeight = "3"