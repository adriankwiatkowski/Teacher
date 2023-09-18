package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.LessonAttendance

class LessonAttendancesPreviewParameterProvider : PreviewParameterProvider<List<LessonAttendance>> {
    override val values: Sequence<List<LessonAttendance>> = sequenceOf(data)
}

private val data: List<LessonAttendance> = BasicStudentsPreviewParameterProvider()
    .values
    .first()
    .mapIndexed { index, student ->
        LessonAttendance(
            eventId = 1L,
            isCancelled = index % 2 == 1,
            student = student,
            attendance = Attendance.entries[index % Attendance.entries.size],
        )
    }