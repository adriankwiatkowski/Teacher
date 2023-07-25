package com.example.teacherapp.feature.lesson.grade.data

import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField
import java.math.BigDecimal

internal data class GradeForm(
    val grade: InputField<BigDecimal?>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = grade.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}