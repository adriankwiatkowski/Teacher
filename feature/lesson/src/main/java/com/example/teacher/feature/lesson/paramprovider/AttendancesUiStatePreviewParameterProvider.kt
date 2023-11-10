package com.example.teacher.feature.lesson.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.ui.paramprovider.LessonEventAttendancesPreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.feature.lesson.attendance.data.AttendancesUiState

internal class AttendancesUiStatePreviewParameterProvider :
    PreviewParameterProvider<AttendancesUiState> {

    override val values: Sequence<AttendancesUiState>
        get() {
            val lesson = LessonPreviewParameterProvider().values.first()
            val schoolYear = lesson.schoolClass.schoolYear

            val attendances = LessonEventAttendancesPreviewParameterProvider().values.first()

            val firstTermScheduleAttendances = attendances.filter { attendance ->
                val term = schoolYear.firstTerm
                TimeUtils.isBetween(attendance.date, term.startDate, term.endDate)
            }
            val secondTermScheduleAttendances = attendances.filter { attendance ->
                val term = schoolYear.secondTerm
                TimeUtils.isBetween(attendance.date, term.startDate, term.endDate)
            }
            val scheduleAttendancesWithoutTerm = attendances.filter { attendance ->
                val isInFirstTerm = attendance in firstTermScheduleAttendances
                val isInSecondTerm = attendance in secondTermScheduleAttendances
                !isInFirstTerm && !isInSecondTerm
            }

            return sequenceOf(
                AttendancesUiState(
                    firstTermScheduleAttendances = firstTermScheduleAttendances,
                    secondTermScheduleAttendances = secondTermScheduleAttendances,
                    scheduleAttendancesWithoutTerm = scheduleAttendancesWithoutTerm,
                ),
                AttendancesUiState(
                    firstTermScheduleAttendances = attendances,
                    secondTermScheduleAttendances = emptyList(),
                    scheduleAttendancesWithoutTerm = emptyList(),
                ),
                AttendancesUiState(
                    firstTermScheduleAttendances = emptyList(),
                    secondTermScheduleAttendances = emptyList(),
                    scheduleAttendancesWithoutTerm = emptyList(),
                )
            )
        }
}