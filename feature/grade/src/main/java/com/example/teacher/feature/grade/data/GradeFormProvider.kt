package com.example.teacher.feature.grade.data

import com.example.teacher.core.common.utils.GradeUtils
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import java.math.BigDecimal

internal object GradeFormProvider {

    fun validateGrade(grade: BigDecimal?, isEdited: Boolean = true): InputField<BigDecimal?> {
        val isError = grade == null || grade < GradeUtils.MinGrade || grade > GradeUtils.MaxGrade
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