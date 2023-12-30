package com.example.teacher.feature.student

import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.datasource.event.EventDataSource
import com.example.teacher.core.database.datasource.grade.GradeDataSource
import com.example.teacher.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.lessonattendance.LessonAttendanceDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.database.datasource.studentnote.StudentNoteDataSource
import com.example.teacher.core.database.model.EventDto
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term
import java.math.BigDecimal
import java.time.LocalDate

internal suspend fun givenGrade(
    gradeDataSource: GradeDataSource,
    gradeTemplateId: Long,
    studentId: Long,
    id: Long? = null,
    grade: BigDecimal = DecimalUtils.Five,
) {
    gradeDataSource.insertOrUpdateGrade(
        id = id,
        gradeTemplateId = gradeTemplateId,
        studentId = studentId,
        grade = grade,
    )
}

internal suspend fun givenGradeTemplate(
    gradeTemplateDataSource: GradeTemplateDataSource,
    lessonId: Long,
    id: Long? = null,
    name: String = "Grade Template",
    description: String? = null,
    weight: Int = 3,
    isFirstTerm: Boolean = true,
) {
    gradeTemplateDataSource.insertOrUpdateGradeTemplate(
        id = id,
        lessonId = lessonId,
        name = name,
        description = description,
        weight = weight,
        isFirstTerm = isFirstTerm,
    )
}

internal suspend fun givenStudentNote(
    studentNoteDataSource: StudentNoteDataSource,
    studentId: Long,
    id: Long? = null,
    title: String = "Title",
    description: String = "",
    isNegative: Boolean = false,
) {
    studentNoteDataSource.insertOrUpdateStudentNote(
        id = id,
        studentId = studentId,
        title = title,
        description = description,
        isNegative = isNegative,
    )
}

internal suspend fun givenStudent(
    studentDataSource: StudentDataSource,
    schoolClassId: Long,
    id: Long? = null,
    name: String = "Name",
    surname: String = "Surname",
    email: String? = null,
    phone: String? = null,
) {
    studentDataSource.insertOrUpdateStudent(
        id = id,
        schoolClassId = schoolClassId,
        registerNumber = null,
        name = name,
        surname = surname,
        email = email,
        phone = phone,
    )
}

internal suspend fun givenLessonAttendance(
    lessonAttendanceDataSource: LessonAttendanceDataSource,
    eventId: Long,
    studentId: Long,
    attendance: Attendance = Attendance.Present,
) {
    lessonAttendanceDataSource.insertOrUpdateLessonAttendance(
        eventId = eventId,
        studentId = studentId,
        attendance = attendance,
    )
}

internal suspend fun givenEvent(
    eventDataSource: EventDataSource,
    id: Long? = null,
    lessonId: Long? = null,
    date: LocalDate = TimeUtils.currentDate(),
    name: String = "Event",
    isCancelled: Boolean = false,
) {
    eventDataSource.insertEvents(
        listOf(
            EventDto(
                id = id,
                lessonId = lessonId,
                name = name,
                date = date,
                startTime = TimeUtils.currentTime(),
                endTime = TimeUtils.plusTime(TimeUtils.currentTime(), 0, 45),
                isCancelled = isCancelled,
            )
        )
    )
}

internal suspend fun givenLesson(
    lessonDataSource: LessonDataSource,
    schoolClassId: Long,
    id: Long? = null,
    name: String = "Name",
) {
    lessonDataSource.insertOrUpdateLesson(id = id, schoolClassId = schoolClassId, name = name)
}

internal suspend fun givenSchoolClass(
    schoolClassDataSource: SchoolClassDataSource,
    schoolYearDataSource: SchoolYearDataSource,
    basicSchoolClass: BasicSchoolClass,
) {
    schoolYearDataSource.insertOrUpdateSchoolYear(
        id = null,
        schoolYearName = basicSchoolClass.schoolYear.name,
        termFirstName = basicSchoolClass.schoolYear.firstTerm.name,
        termFirstStartDate = basicSchoolClass.schoolYear.firstTerm.startDate,
        termFirstEndDate = basicSchoolClass.schoolYear.firstTerm.endDate,
        termSecondName = basicSchoolClass.schoolYear.secondTerm.name,
        termSecondStartDate = basicSchoolClass.schoolYear.secondTerm.startDate,
        termSecondEndDate = basicSchoolClass.schoolYear.secondTerm.endDate,
    )

    schoolClassDataSource.insertOrUpdateSchoolClass(
        id = null,
        schoolYearId = basicSchoolClass.schoolYear.id,
        name = basicSchoolClass.name
    )
}

internal fun testSchoolClass(
    id: Long = 0,
    name: String = "Name",
) = BasicSchoolClass(
    id = id,
    name = name,
    schoolYear = SchoolYear(
        1L,
        name = "School Year",
        firstTerm = Term(
            1L,
            name = "Term I",
            startDate = TimeUtils.currentDate(),
            endDate = TimeUtils.plusDays(TimeUtils.currentDate(), 1)
        ),
        secondTerm = Term(
            2L,
            name = "Term II",
            startDate = TimeUtils.plusDays(TimeUtils.currentDate(), 2),
            endDate = TimeUtils.plusDays(TimeUtils.currentDate(), 3),
        ),
    ),
    studentCount = 0L,
    lessonCount = 0L,
)