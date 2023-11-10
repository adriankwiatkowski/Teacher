package com.example.teacher.core.database.querymapper

import com.example.teacher.core.common.utils.DecimalUtils.safeDivide
import com.example.teacher.core.common.utils.DecimalUtils.toPercentage
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.generated.queries.lessonattendance.GetLessonAttendancesByEventId
import com.example.teacher.core.database.generated.queries.lessonattendance.GetLessonEventsByLessonId
import com.example.teacher.core.database.generated.queries.lessonattendance.GetSchoolYearByLessonId
import com.example.teacher.core.database.generated.queries.lessonattendance.GetStudentsWithAttendanceByLessonId
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.LessonAttendance
import com.example.teacher.core.model.data.LessonEventAttendance
import com.example.teacher.core.model.data.StudentWithAttendance
import java.math.BigDecimal

internal fun toExternal(
    attendances: List<GetLessonAttendancesByEventId>
): List<LessonAttendance> = attendances.map { lessonAttendance ->
    val attendance =
        lessonAttendance.attendance_text?.let { attendance -> Attendance.of(attendance) }

    LessonAttendance(
        eventId = lessonAttendance.event_id,
        isCancelled = lessonAttendance.event_is_cancelled,
        student = BasicStudent(
            id = lessonAttendance.student_id,
            classId = lessonAttendance.school_class_id,
            registerNumber = lessonAttendance.student_register_number,
            name = lessonAttendance.student_name,
            surname = lessonAttendance.student_surname,
            email = lessonAttendance.student_email,
            phone = lessonAttendance.student_phone,
        ),
        attendance = attendance,
    )
}

internal fun toExternalLessonEventAttendances(
    events: List<GetLessonEventsByLessonId>
): List<LessonEventAttendance> = events.map { event ->
    val attendanceNotSetCount = (
            event.student_count
                    - event.present_count
                    - event.late_count
                    - event.absent_count
                    - event.excused_absence_count
                    - event.exemption_count
            )

    LessonEventAttendance(
        eventId = event.event_id,
        date = event.event_date,
        startTime = event.event_start_time,
        endTime = event.event_end_time,
        isCancelled = event.event_is_cancelled,
        presentCount = event.present_count,
        lateCount = event.late_count,
        absentCount = event.absent_count,
        excusedAbsenceCount = event.excused_absence_count,
        exemptionCount = event.exemption_count,
        attendanceNotSetCount = attendanceNotSetCount,
    )
}

internal fun toStudentsWithAttendanceExternal(
    attendances: List<GetStudentsWithAttendanceByLessonId>,
    schoolYear: GetSchoolYearByLessonId,
): List<StudentWithAttendance> {
    val presentAttendancesValues =
        setOf(Attendance.Present, Attendance.Late, Attendance.ExcusedAbsence, Attendance.Exemption)

    return attendances.groupBy { attendance -> attendance.student_id }
        .map { (_, attendances) ->
            val firstTermAttendances = attendances.filter { attendance ->
                attendance.attendance_text != null && !attendance.event_is_cancelled && TimeUtils.isBetween(
                    attendance.event_date,
                    schoolYear.term_first_start_date,
                    schoolYear.term_first_end_date,
                )
            }
            val secondTermAttendances = attendances.filter { attendance ->
                attendance.attendance_text != null && !attendance.event_is_cancelled && TimeUtils.isBetween(
                    attendance.event_date,
                    schoolYear.term_second_start_date,
                    schoolYear.term_second_end_date,
                )
            }

            val firstTermAverageAttendancePercentage =
                getAverageAttendancePercentage(firstTermAttendances, presentAttendancesValues)
            val secondTermAverageAttendancePercentage =
                getAverageAttendancePercentage(secondTermAttendances, presentAttendancesValues)

            val studentData = attendances.first()
            StudentWithAttendance(
                student = BasicStudent(
                    id = studentData.student_id,
                    classId = studentData.school_class_id,
                    registerNumber = studentData.student_register_number,
                    name = studentData.student_name,
                    surname = studentData.student_surname,
                    email = studentData.student_email,
                    phone = studentData.student_phone,
                ),
                firstTermAverageAttendancePercentage = firstTermAverageAttendancePercentage,
                secondTermAverageAttendancePercentage = secondTermAverageAttendancePercentage,
            )
        }
}

private fun getAverageAttendancePercentage(
    attendances: List<GetStudentsWithAttendanceByLessonId>,
    presentAttendancesValues: Set<Attendance>,
) = if (attendances.isEmpty()) {
    BigDecimal.valueOf(1L).toPercentage()
} else {
    val presentAttendances = attendances.filter { attendance ->
        val actualAttendance = attendance.attendance_text?.let { text -> Attendance.of(text) }
        actualAttendance in presentAttendancesValues
    }

    BigDecimal.valueOf(presentAttendances.size.toLong())
        .safeDivide(BigDecimal.valueOf(attendances.size.toLong()))
        .toPercentage()
}