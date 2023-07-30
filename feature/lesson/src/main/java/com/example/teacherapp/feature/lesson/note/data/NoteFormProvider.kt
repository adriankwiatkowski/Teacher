package com.example.teacherapp.feature.lesson.note.data

import androidx.core.text.trimmedLength
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

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
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): NoteForm {
        val actualTitle = title.ifNull {
            "Notatka z dnia ${TimeUtils.format(TimeUtils.currentDate())}"
        }

        return NoteForm(
            title = validateTitle(actualTitle, isEdited = isEdited),
            text = validateText(text, isEdited = isEdited),
            status = status,
        )
    }
}

private inline fun String?.ifNull(defaultValue: () -> String): String = this ?: defaultValue()