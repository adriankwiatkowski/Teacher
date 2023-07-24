package com.example.teacherapp.ui.screens.schoolyear.data

import com.example.teacherapp.core.ui.model.InputField
import com.example.teacherapp.data.models.input.InputDate
import java.util.UUID

data class TermForm(
    val name: InputField<String>,
    val startDate: InputDate,
    val endDate: InputDate,
    val formId: Any = UUID.randomUUID(),
)