package com.example.teacherapp.feature.schoolyear.data

import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

internal data class SchoolYearForm(
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

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}