package com.example.teacher.feature.schedule.data

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.ui.model.FormStatus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.min

internal object EventFormProvider {

    fun sanitizeDay(day: DayOfWeek): DayOfWeek {
        return day
    }

    fun sanitizeDate(date: LocalDate): LocalDate {
        return date
    }

    @Suppress("NAME_SHADOWING")
    fun sanitizeStartTime(startTime: LocalTime, endTime: LocalTime): TimeData {
        require(DefaultMinuteDiff in 1..59)

        var startTime = startTime
        var endTime = endTime

        val startTimeHour = TimeUtils.localTimeHour(startTime)
        var startTimeMinute = TimeUtils.localTimeMinute(startTime)

        if (startTimeHour == 23 && startTimeMinute == 59) {
            startTime = TimeUtils.localTimeOf(23, 58)
            startTimeMinute = TimeUtils.localTimeMinute(startTime)
        }

        if (!TimeUtils.isBefore(startTime, endTime)) {
            // Don't allow end time to be before start time.
            val minutesToAdd = if (startTimeHour == 23) {
                min(DefaultMinuteDiff, 60 - 1 - startTimeMinute)
            } else {
                DefaultMinuteDiff
            }.toLong()
            endTime = TimeUtils.plusTime(startTime, 0, minutesToAdd)
        }

        return TimeData(startTime, endTime)
    }

    @Suppress("NAME_SHADOWING")
    fun sanitizeEndTime(startTime: LocalTime, endTime: LocalTime): TimeData {
        var startTime = startTime
        var endTime = endTime

        if (TimeUtils.localTimeHour(endTime) == 0 && TimeUtils.localTimeMinute(endTime) == 0) {
            endTime = TimeUtils.localTimeOf(0, 1)
        }

        if (!TimeUtils.isAfter(endTime, startTime)) {
            startTime = TimeUtils.minusTime(endTime, 0, 1)
        }

        return TimeData(startTime, endTime)
    }

    fun createDefaultForm(
        day: DayOfWeek = TimeUtils.monday(),
        date: LocalDate = TimeUtils.currentDate(),
        startTime: LocalTime = TimeUtils.localTimeOf(8, 0),
        endTime: LocalTime = TimeUtils.plusTime(startTime, hours = 0, minutes = 45),
        isFirstTermSelected: Boolean = true,
        type: EventType = EventType.Weekly,
        status: FormStatus = FormStatus.Idle,
    ): EventForm {
        val timeData = sanitizeStartTime(startTime, endTime)

        return EventForm(
            day = sanitizeDay(day),
            date = sanitizeDate(date),
            startTime = timeData.startTime,
            endTime = timeData.endTime,
            isFirstTermSelected = isFirstTermSelected,
            type = type,
            isValid = false,
            status = status,
        )
    }

    private const val DefaultMinuteDiff = 45
}

internal data class TimeData(val startTime: LocalTime, val endTime: LocalTime)