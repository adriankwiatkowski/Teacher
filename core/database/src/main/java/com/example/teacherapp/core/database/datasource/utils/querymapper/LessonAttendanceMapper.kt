package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lessonattendance.GetLessonAttendancesByLessonScheduleId
import com.example.teacherapp.core.database.generated.queries.lessonattendance.GetLessonSchedulesByLessonId
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonScheduleAttendance

internal fun toExternalLessonScheduleAttendances(
    schedules: List<GetLessonSchedulesByLessonId>
): List<LessonScheduleAttendance> = schedules.map { schedule ->
    val attendanceNotSetCount = (
            schedule.student_count
                    - schedule.present_count
                    - schedule.late_count
                    - schedule.absent_count
                    - schedule.excused_absence_count
                    - schedule.exemption_count
            )

    LessonScheduleAttendance(
        lessonScheduleId = schedule.lesson_schedule_id,
        date = schedule.lesson_schedule_date,
        startTime = schedule.lesson_schedule_start_time,
        endTime = schedule.lesson_schedule_end_time,
        isValid = schedule.lesson_schedule_is_valid,
        presentCount = schedule.present_count,
        lateCount = schedule.late_count,
        absentCount = schedule.absent_count,
        excusedAbsenceCount = schedule.excused_absence_count,
        exemptionCount = schedule.exemption_count,
        attendanceNotSetCount = attendanceNotSetCount,
    )
}

internal fun toExternal(
    attendances: List<GetLessonAttendancesByLessonScheduleId>
): List<LessonAttendance> = attendances.map { lessonAttendance ->
    val attendance =
        lessonAttendance.attendance_text?.let { attendance -> Attendance.of(attendance) }

    LessonAttendance(
        lessonScheduleId = lessonAttendance.lesson_schedule_id,
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