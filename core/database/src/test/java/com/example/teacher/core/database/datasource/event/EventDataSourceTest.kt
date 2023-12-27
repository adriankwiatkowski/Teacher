package com.example.teacher.core.database.datasource.event

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSourceImpl
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSourceImpl
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSourceImpl
import com.example.teacher.core.database.model.EventDto
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class EventDataSourceTest {

    private lateinit var eventDataSource: EventDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource
    private lateinit var lessonDataSource: LessonDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        eventDataSource = EventDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun givenEvent_getEvents_happyPath() = runTest {
        val date = TimeUtils.currentDate()
        givenEvent(date = date)

        val events = eventDataSource.getEvents(date).first()

        assertEquals(1, events.size)
    }

    @Test
    fun givenEvent_getEventById() = runTest {
        val date = TimeUtils.currentDate()
        givenEvent(date = date)
        val id = eventDataSource.getEvents(date).first().first().id

        val event = eventDataSource.getEventById(id).first()

        assertNotNull(event)
        assertEquals(id, event?.id)
    }

    @Test
    fun insertEvents() = runTest {
        val date = TimeUtils.currentDate()
        givenEvent(date = date)

        val events = eventDataSource.getEvents(date).first()

        assertEquals(1, events.size)
    }

    @Test
    fun updateEvent() = runTest {
        givenSchoolClass(testSchoolClass())
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenLesson(schoolClassId = 2L)
        val date = TimeUtils.currentDate()
        givenEvent(date = date, lessonId = 1L)
        val oldEvent = eventDataSource.getEvents(date).first().first()

        eventDataSource.updateEvent(
            id = oldEvent.id,
            lessonId = 2L,
            name = oldEvent.name.orEmpty(),
            date = oldEvent.date,
            startTime = oldEvent.startTime,
            endTime = oldEvent.endTime,
            isCancelled = oldEvent.isCancelled,
        )

        val event = eventDataSource.getEvents(date).first().first()

        assertNotNull(event)
        assertEquals(2L, event.lesson?.id)
    }

    @Test
    fun deleteEventById() = runTest {
        val date = TimeUtils.currentDate()
        givenEvent(date = date)
        val id = eventDataSource.getEvents(date).first().first().id

        eventDataSource.deleteEventById(id)
        val event = eventDataSource.getEventById(id).first()

        assertNull(event)
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

    private suspend fun givenLesson(schoolClassId: Long) {
        lessonDataSource.insertOrUpdateLesson(
            id = null,
            schoolClassId = schoolClassId,
            name = "Lesson",
        )
    }

    private suspend fun givenSchoolClass(basicSchoolClass: BasicSchoolClass) {
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