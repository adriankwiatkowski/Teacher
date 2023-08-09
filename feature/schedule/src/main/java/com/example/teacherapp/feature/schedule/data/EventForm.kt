package com.example.teacherapp.feature.schedule.data

import com.example.teacherapp.core.model.data.EventType
import com.example.teacherapp.core.ui.model.FormStatus
import java.time.LocalDate
import java.time.LocalTime

internal data class EventForm(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val type: EventType,
    val isValid: Boolean,
    val status: FormStatus,
) {
    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}