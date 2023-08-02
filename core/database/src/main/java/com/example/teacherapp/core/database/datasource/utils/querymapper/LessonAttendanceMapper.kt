package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.lessonattendance.GetLessonAttendancesByLessonId
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.LessonAttendance

internal fun toExternal(
    attendances: List<GetLessonAttendancesByLessonId>
): List<LessonAttendance> = attendances.map { attendance ->
    LessonAttendance(
        lessonScheduleId = attendance.lesson_schedule_id,
        student = BasicStudent(
            id = attendance.student_id,
            classId = attendance.school_class_id,
            orderInClass = attendance.student_order_in_class,
            name = attendance.student_name,
            surname = attendance.student_surname,
            email = attendance.student_email,
            phone = attendance.student_phone,
        ),
        attendance = Attendance.of(attendance.attendance_text),
    )
}