package com.example.teacherapp.ui.screens.grade.data

import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import java.math.BigDecimal

data class GradeForm(
    val grade: InputField<BigDecimal?>,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = grade.isValid

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}