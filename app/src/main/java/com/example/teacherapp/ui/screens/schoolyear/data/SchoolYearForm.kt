package com.example.teacherapp.ui.screens.schoolyear.data

import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField

data class SchoolYearForm(
    val schoolYearName: InputField<String>,
    val termForms: List<TermForm>,
    val status: FormStatus,
) {
    val isValid: Boolean by lazy {
        val isSchoolYearNameValid = !schoolYearName.isError
        val isTermFormsValid = termForms.all { termForm ->
            !termForm.name.isError
        }

        isSchoolYearNameValid && isTermFormsValid
    }

    val canSubmit: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}