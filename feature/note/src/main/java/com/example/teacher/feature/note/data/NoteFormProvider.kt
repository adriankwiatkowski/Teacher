package com.example.teacher.feature.note.data

import androidx.core.text.trimmedLength
import com.example.teacher.core.model.data.NotePriority
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal object NoteFormProvider {

    fun validateTitle(title: String, isEdited: Boolean = true): InputField<String> {
        val trimmedLength = title.trimmedLength()
        val charCountLimit = 120
        return InputField(
            title,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 1..charCountLimit,
            isEdited = isEdited,
        )
    }

    fun validateText(description: String, isEdited: Boolean = true): InputField<String> {
        return InputField(
            description,
            isEdited = isEdited,
        )
    }

    fun createDefaultForm(
        title: String? = null,
        text: String = "",
        priority: NotePriority = NotePriority.Medium,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): NoteForm {
        return NoteForm(
            title = validateTitle(title.orEmpty(), isEdited = isEdited),
            text = validateText(text, isEdited = isEdited),
            priority = priority,
            status = status,
        )
    }
}