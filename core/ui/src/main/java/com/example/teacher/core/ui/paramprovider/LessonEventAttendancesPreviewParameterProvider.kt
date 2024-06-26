package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.LessonEventAttendance
import java.time.LocalTime

class LessonEventAttendancesPreviewParameterProvider :
    PreviewParameterProvider<List<LessonEventAttendance>> {
    override val values: Sequence<List<LessonEventAttendance>> = sequenceOf(data, emptyList())
}

private val data = makeData(
    SimpleLessonEventAttendance(
        startTime = TimeUtils.localTimeOf(8, 0),
        presentCount = 14,
        lateCount = 4,
        absentCount = 2,
        excusedAbsenceCount = 0,
        exemptionCount = 0,
        attendanceNotSetCount = 0,
    ),
    SimpleLessonEventAttendance(
        startTime = TimeUtils.localTimeOf(8, 0),
        presentCount = 14,
        lateCount = 4,
        absentCount = 2,
        excusedAbsenceCount = 0,
        exemptionCount = 0,
        attendanceNotSetCount = 0,
    ),
    SimpleLessonEventAttendance(
        startTime = TimeUtils.localTimeOf(8, 0),
        presentCount = 18,
        lateCount = 0,
        absentCount = 0,
        excusedAbsenceCount = 2,
        exemptionCount = 0,
        attendanceNotSetCount = 0,
    ),
    SimpleLessonEventAttendance(
        startTime = TimeUtils.localTimeOf(8, 0),
        presentCount = 18,
        lateCount = 0,
        absentCount = 0,
        excusedAbsenceCount = 0,
        exemptionCount = 0,
        attendanceNotSetCount = 2,
    ),
    SimpleLessonEventAttendance(
        startTime = TimeUtils.localTimeOf(8, 0),
        presentCount = 18,
        lateCount = 0,
        absentCount = 0,
        excusedAbsenceCount = 0,
        exemptionCount = 2,
        attendanceNotSetCount = 0,
    ),
    SimpleLessonEventAttendance(
        startTime = TimeUtils.localTimeOf(14, 45),
        presentCount = 20,
        lateCount = 0,
        absentCount = 0,
        excusedAbsenceCount = 0,
        exemptionCount = 0,
        attendanceNotSetCount = 0,
    ),
)

private fun makeData(
    vararg simpleLessonEventAttendances: SimpleLessonEventAttendance,
): List<LessonEventAttendance> {
    return simpleLessonEventAttendances.mapIndexed { index, simpleLessonScheduleAttendance ->
        LessonEventAttendance(
            eventId = index.toLong() + 1L,
            date = TimeUtils.plusDays(TimeUtils.currentDate(), index.toLong()),
            startTime = simpleLessonScheduleAttendance.startTime,
            endTime = TimeUtils.plusTime(simpleLessonScheduleAttendance.startTime, 0, 45),
            isCancelled = index % 2 == 0,
            presentCount = simpleLessonScheduleAttendance.presentCount,
            lateCount = simpleLessonScheduleAttendance.lateCount,
            absentCount = simpleLessonScheduleAttendance.absentCount,
            excusedAbsenceCount = simpleLessonScheduleAttendance.excusedAbsenceCount,
            exemptionCount = simpleLessonScheduleAttendance.exemptionCount,
            attendanceNotSetCount = simpleLessonScheduleAttendance.attendanceNotSetCount,
        )
    }
}

private data class SimpleLessonEventAttendance(
    val startTime: LocalTime,
    val presentCount: Long,
    val lateCount: Long,
    val absentCount: Long,
    val excusedAbsenceCount: Long,
    val exemptionCount: Long,
    val attendanceNotSetCount: Long,
)