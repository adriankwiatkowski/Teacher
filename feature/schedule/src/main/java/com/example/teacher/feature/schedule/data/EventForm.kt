package com.example.teacher.feature.schedule.data

import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.ui.model.FormStatus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

internal data class EventForm(
    val day: DayOfWeek,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isCancelled: Boolean,
    val isFirstTermSelected: Boolean,
    val type: EventType,
    val isValid: Boolean,
    val status: FormStatus,
) {
    val isSubmitEnabled: Boolean
        get() = isValid && status != FormStatus.Saving && status != FormStatus.Success
}