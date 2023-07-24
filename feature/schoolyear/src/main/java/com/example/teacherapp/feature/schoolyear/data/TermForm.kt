package com.example.teacherapp.feature.schoolyear.data

import com.example.teacherapp.core.ui.model.InputField
import java.util.UUID

internal data class TermForm(
    val name: InputField<String>,
    val startDate: InputDate,
    val endDate: InputDate,
    val formId: Any = UUID.randomUUID(),
)