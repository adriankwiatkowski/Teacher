package com.example.teacherapp.ui.screens.grade.data

import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import java.math.BigDecimal

object GradeFormProvider {

    fun validateGrade(grade: BigDecimal?, isEdited: Boolean = true): InputField<BigDecimal?> {
        val isError = grade == null || grade < MinGrade || grade > MaxGrade
        return InputField(
            grade,
            isError = isError,
            isEdited = isEdited,
        )
    }

    fun createDefaultForm(
        grade: BigDecimal? = null,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): GradeForm {
        return GradeForm(
            grade = validateGrade(grade, isEdited = isEdited),
            status = status,
        )
    }
}

private val MinGrade = BigDecimal("1.00")
private val MaxGrade = BigDecimal("6.00")