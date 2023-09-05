package com.example.teacher.feature.schoolclass.data

import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField

internal data class SchoolClassForm(
    val id: Long?,
    val schoolClassName: InputField<String>,
    val schoolYear: InputField<SchoolYear?>,
    val status: FormStatus,
) {
    private val isValid: Boolean
        get() = schoolClassName.isValid && schoolYear.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}