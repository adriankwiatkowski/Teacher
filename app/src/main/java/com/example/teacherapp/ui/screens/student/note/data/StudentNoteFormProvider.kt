package com.example.teacherapp.ui.screens.student.note.data

import androidx.core.text.trimmedLength
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

object StudentNoteFormProvider {

    fun validateTitle(title: String, isEdited: Boolean = true): InputField<String> {
        val trimmedLength = title.trimmedLength()
        val charCountLimit = 60
        return InputField(
            title,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 1..charCountLimit,
            isEdited = isEdited,
        )
    }

    fun validateDescription(description: String?, isEdited: Boolean = true): InputField<String?> {
        val trimmedLength = description?.trimmedLength() ?: 0
        val charCountLimit = 60
        return InputField(
            description,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 0..charCountLimit,
            isEdited = isEdited,
            isRequired = false,
        )
    }

    fun createDefaultForm(
        title: String = "",
        description: String? = null,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): StudentNoteForm {
        return StudentNoteForm(
            title = validateTitle(title, isEdited = isEdited),
            description = validateDescription(description, isEdited = isEdited),
            status = status,
        )
    }
}