package com.example.teacherapp.feature.schoolclass.data

import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField

internal data class SchoolClassForm(
    val id: Long?,
    val schoolClassName: InputField<String>,
    val schoolYear: InputField<SchoolYear?>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = schoolClassName.isValid && schoolYear.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}