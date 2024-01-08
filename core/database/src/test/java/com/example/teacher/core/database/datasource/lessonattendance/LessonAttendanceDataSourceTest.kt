package com.example.teacher.core.database.datasource.lessonattendance

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.database.datasource.event.EventDataSource
import com.example.teacher.core.database.datasource.event.EventDataSourceImpl
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSourceImpl
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSourceImpl
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSourceImpl
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSourceImpl
import com.example.teacher.core.database.model.EventDto
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class LessonAttendanceDataSourceTest {

    private lateinit var lessonAttendanceDataSource: LessonAttendanceDataSource
    private lateinit var eventDataSource: EventDataSource
    private lateinit var lessonDataSource: LessonDataSource
    private lateinit var studentDataSource: StudentDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        lessonAttendanceDataSource = LessonAttendanceDataSourceImpl(db, testDispatcher)
        eventDataSource = EventDataSourceImpl(db, testDispatcher)
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
        studentDataSource = StudentDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getLessonEventAttendancesByLessonId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)
        val date = TimeUtils.currentDate()
        givenEvent(date = date, lessonId = 1L)
        givenLessonAttendance(eventId = 1L, studentId = 1L)

        val attendances = lessonAttendanceDataSource.getLessonEventAttendancesByLessonId(1L).first()

        assertEquals(1, attendances.size)
    }

    @Test
    fun getLessonAttendancesByEventId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)
        val date = TimeUtils.currentDate()
        givenEvent(date = date, lessonId = 1L)
        givenLessonAttendance(eventId = 1L, studentId = 1L)

        val attendances = lessonAttendanceDataSource.getLessonAttendancesByEventId(1L).first()

        assertEquals(1, attendances.size)
    }

    @Test
    fun getStudentsWithAttendanceByLessonId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)

        givenEvent(lessonId = 1L)
        givenLessonAttendance(eventId = 1L, studentId = 1L, attendance = Attendance.Present)
        givenLessonAttendance(eventId = 1L, studentId = 2L, attendance = Attendance.Present)

        givenEvent(lessonId = 1L)
        givenLessonAttendance(eventId = 2L, studentId = 1L, attendance = Attendance.Present)
        givenLessonAttendance(eventId = 2L, studentId = 2L, attendance = Attendance.Absent)

        val actual = lessonAttendanceDataSource.getStudentsWithAttendanceByLessonId(1L).first()
        val first = actual[0]
        val second = actual[1]

        assertEquals(2, actual.size)
        assertEquals(BigDecimal("100.00"), first.firstTermAverageAttendancePercentage)
        assertEquals(BigDecimal("50.00"), second.firstTermAverageAttendancePercentage)
    }

    @Test
    fun insertLessonAttendance() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)
        val date = TimeUtils.currentDate()
        givenEvent(date = date, lessonId = 1L)
        givenLessonAttendance(eventId = 1L, studentId = 1L, attendance = Attendance.Present)

        val attendances = lessonAttendanceDataSource.getLessonEventAttendancesByLessonId(1L).first()

        assertEquals(1, attendances.size)
        assertEquals(1L, attendances.first().eventId)
        assertEquals(1L, attendances.first().presentCount)
    }

    @Test
    fun updateLessonAttendance() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)
        val date = TimeUtils.currentDate()
        givenEvent(date = date, lessonId = 1L)
        givenLessonAttendance(eventId = 1L, studentId = 1L, attendance = Attendance.Present)

        givenLessonAttendance(eventId = 1L, studentId = 1L, attendance = Attendance.ExcusedAbsence)
        val attendances = lessonAttendanceDataSource.getLessonEventAttendancesByLessonId(1L).first()

        assertEquals(1, attendances.size)
        assertEquals(1L, attendances.first().eventId)
        assertEquals(0L, attendances.first().presentCount)
        assertEquals(1L, attendances.first().excusedAbsenceCount)
    }

    @Test
    fun deleteLessonAttendance() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)
        val date = TimeUtils.currentDate()
        givenEvent(date = date, lessonId = 1L)
        givenLessonAttendance(eventId = 1L, studentId = 1L)

        lessonAttendanceDataSource.deleteLessonAttendance(eventId = 1L, studentId = 1L)
        val attendances =
            lessonAttendanceDataSource.getLessonEventAttendancesByLessonId(1L).first().first()

        assertEquals(0L, attendances.presentCount)
    }

    private suspend fun givenLessonAttendance(
        eventId: Long,
        studentId: Long,
        attendance: Attendance = Attendance.Present,
    ) {
        lessonAttendanceDataSource.upsertLessonAttendance(
            eventId = eventId,
            studentId = studentId,
            attendance = attendance,
        )
    }

    private suspend fun givenEvent(
        id: Long? = null,
        lessonId: Long? = null,
        date: LocalDate = TimeUtils.currentDate(),
    ) {
        eventDataSource.insertEvents(
            listOf(
                EventDto(
                    id = id,
                    lessonId = lessonId,
                    name = "Event",
                    date = date,
                    startTime = TimeUtils.currentTime(),
                    endTime = TimeUtils.plusTime(TimeUtils.currentTime(), 0, 45),
                    isCancelled = false,
                )
            )
        )
    }

    private suspend fun givenLesson(
        schoolClassId: Long,
        id: Long? = null,
        name: String = "Name",
    ) {
        lessonDataSource.upsertLesson(id = id, schoolClassId = schoolClassId, name = name)
    }

    private suspend fun givenStudent(
        schoolClassId: Long,
        id: Long? = null,
        name: String = "Name",
        surname: String = "Surname",
        email: String? = null,
        phone: String? = null,
    ) {
        studentDataSource.upsertStudent(
            id = id,
            schoolClassId = schoolClassId,
            registerNumber = null,
            name = name,
            surname = surname,
            email = email,
            phone = phone,
        )
    }

    private suspend fun givenSchoolClass(basicSchoolClass: BasicSchoolClass) {
        schoolYearDataSource.upsertSchoolYear(
            id = null,
            schoolYearName = basicSchoolClass.schoolYear.name,
            termFirstName = basicSchoolClass.schoolYear.firstTerm.name,
            termFirstStartDate = basicSchoolClass.schoolYear.firstTerm.startDate,
            termFirstEndDate = basicSchoolClass.schoolYear.firstTerm.endDate,
            termSecondName = basicSchoolClass.schoolYear.secondTerm.name,
            termSecondStartDate = basicSchoolClass.schoolYear.secondTerm.startDate,
            termSecondEndDate = basicSchoolClass.schoolYear.secondTerm.endDate,
        )

        schoolClassDataSource.upsertSchoolClass(
            id = null,
            schoolYearId = basicSchoolClass.schoolYear.id,
            name = basicSchoolClass.name
        )
    }

    private fun testSchoolClass(
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
}