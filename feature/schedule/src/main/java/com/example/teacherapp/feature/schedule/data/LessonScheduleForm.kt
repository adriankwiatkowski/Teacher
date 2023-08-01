package com.example.teacherapp.feature.schedule.data

import com.example.teacherapp.core.model.data.LessonScheduleType
import com.example.teacherapp.core.ui.model.FormStatus
import java.time.LocalDate
import java.time.LocalTime

internal data class LessonScheduleForm(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val type: LessonScheduleType,
    val status: FormStatus,
) {
    val isValid: Boolean
        get() = true

    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}