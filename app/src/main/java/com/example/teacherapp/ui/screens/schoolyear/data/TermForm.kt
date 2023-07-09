package com.example.teacherapp.ui.screens.schoolyear.data

import com.example.teacherapp.data.models.input.InputDate
import com.example.teacherapp.data.models.input.InputField
import java.util.*

data class TermForm(
    val name: InputField<String>,
    val startDate: InputDate,
    val endDate: InputDate,
    val formId: Any = UUID.randomUUID(),
)