package com.example.teacherapp.feature.schoolclass.data

import androidx.core.text.trimmedLength
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

internal object SchoolClassFormProvider {

    fun validateSchoolClassName(
        name: String,
        isEdited: Boolean = true,
    ): InputField<String> {
        val charCountLimit = 60
        val trimmedLength = name.trimmedLength()
        return InputField(
            name,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 1..charCountLimit,
            isEdited = isEdited,
        )
    }

    fun validateSchoolYear(
        schoolYear: SchoolYear?,
        isEdited: Boolean = true,
    ): InputField<SchoolYear?> {
        return InputField(
            schoolYear,
            isError = schoolYear == null,
            isEdited = isEdited,
        )
    }

    fun createDefaultForm(
        id: Long? = null,
        schoolClassName: String = "",
        schoolYear: SchoolYear? = null,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): SchoolClassForm {
        return SchoolClassForm(
            id = id,
            schoolClassName = validateSchoolClassName(name = schoolClassName, isEdited = isEdited),
            schoolYear = validateSchoolYear(schoolYear, isEdited = isEdited),
            status = status,
        )
    }
}