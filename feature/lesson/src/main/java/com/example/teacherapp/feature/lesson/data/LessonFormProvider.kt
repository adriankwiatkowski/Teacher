package com.example.teacherapp.feature.lesson.data

import androidx.core.text.trimmedLength
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

internal object LessonFormProvider {

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

    fun createDefaultForm(
        id: Long? = null,
        name: String = "",
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): LessonForm = LessonForm(
        id = id,
        name = validateName(name, isEdited = isEdited),
        status = status,
    )
}