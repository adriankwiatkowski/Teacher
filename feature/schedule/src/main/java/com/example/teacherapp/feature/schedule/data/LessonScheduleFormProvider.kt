package com.example.teacherapp.feature.schedule.data

import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.model.data.LessonScheduleType
import com.example.teacherapp.core.ui.model.FormStatus
import java.time.LocalDate
import java.time.LocalTime

// TODO: Add validation/sanitization.
internal object LessonScheduleFormProvider {

    fun sanitizeDate(date: LocalDate, isEdited: Boolean = true): LocalDate {
        return date
    }

    fun sanitizeStartTime(startTime: LocalTime, isEdited: Boolean = true): LocalTime {
        return startTime
    }

    fun sanitizeEndTime(endTime: LocalTime, isEdited: Boolean = true): LocalTime {
        return endTime
    }

    fun createDefaultForm(
        date: LocalDate = TimeUtils.currentDate(),
        startTime: LocalTime = TimeUtils.localTimeOf(8, 0),
        endTime: LocalTime = TimeUtils.plusTime(startTime, hours = 0, minutes = 45),
        type: LessonScheduleType = LessonScheduleType.Weekly,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): LessonScheduleForm {
        return LessonScheduleForm(
            date = sanitizeDate(date, isEdited = isEdited),
            startTime = sanitizeStartTime(startTime, isEdited = isEdited),
            endTime = sanitizeEndTime(endTime, isEdited = isEdited),
            type = type,
            status = status,
        )
    }
}