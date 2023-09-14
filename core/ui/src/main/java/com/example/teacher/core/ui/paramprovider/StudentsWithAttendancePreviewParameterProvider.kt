package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.StudentWithAttendance
import java.math.BigDecimal

class StudentsWithAttendancePreviewParameterProvider :
    PreviewParameterProvider<List<StudentWithAttendance>> {
    override val values: Sequence<List<StudentWithAttendance>> = sequenceOf(studentsWithAttendance)
}

private var studentId = 0L
private val student = BasicStudentPreviewParameterProvider().values.first()
private val attendanceAverageRange = 0..100

private val studentsWithAttendance = attendanceAverageRange
    .flatMap { averageFromRange ->
        val step = BigDecimal.valueOf(0.25)
        val steps = 4

        buildList {
            for (i in 0..<steps) {
                val newStudent = student.copy(id = ++studentId)
                var average = BigDecimal.valueOf(averageFromRange.toLong())
                average += step * BigDecimal.valueOf(i.toLong())

                add(
                    StudentWithAttendance(
                        student = newStudent,
                        averageAttendancePercentage = average
                    )
                )

                // Don't add average after 100%.
                if (averageFromRange == 100) {
                    break
                }
            }
        }
    }.asReversed()