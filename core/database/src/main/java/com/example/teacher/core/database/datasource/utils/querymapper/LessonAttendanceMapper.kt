package com.example.teacher.core.database.datasource.utils.querymapper

import com.example.teacher.core.database.generated.queries.lessonattendance.GetLessonAttendancesByEventId
import com.example.teacher.core.database.generated.queries.lessonattendance.GetLessonEventsByLessonId
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.LessonAttendance
import com.example.teacher.core.model.data.LessonEventAttendance

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
        isValid = event.event_is_valid,
        presentCount = event.present_count,
        lateCount = event.late_count,
        absentCount = event.absent_count,
        excusedAbsenceCount = event.excused_absence_count,
        exemptionCount = event.exemption_count,
        attendanceNotSetCount = attendanceNotSetCount,
    )
}

internal fun toExternal(
    attendances: List<GetLessonAttendancesByEventId>
): List<LessonAttendance> = attendances.map { lessonAttendance ->
    val attendance =
        lessonAttendance.attendance_text?.let { attendance -> Attendance.of(attendance) }

    LessonAttendance(
        eventId = lessonAttendance.event_id,
        student = BasicStudent(
            id = lessonAttendance.student_id,
            classId = lessonAttendance.school_class_id,
            orderInClass = lessonAttendance.student_order_in_class,
            name = lessonAttendance.student_name,
            surname = lessonAttendance.student_surname,
            email = lessonAttendance.student_email,
            phone = lessonAttendance.student_phone,
        ),
        attendance = attendance,
    )
}