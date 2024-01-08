package com.example.teacher.core.database.datasource.lessonnote

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSourceImpl
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSourceImpl
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSourceImpl
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class LessonNoteDataSourceTest {

    private lateinit var lessonNoteDataSource: LessonNoteDataSource
    private lateinit var lessonDataSource: LessonDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        lessonNoteDataSource = LessonNoteDataSourceImpl(db, testDispatcher)
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getLessonNotesByLessonId() = runTest {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenNote(lessonId = 1L)

        val notes = lessonNoteDataSource.getLessonNotesByLessonId(1L).first()

        assertEquals(1, notes.size)
    }

    @Test
    fun getLessonNoteById() = runTest {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenNote(lessonId = 1L)

        val note = lessonNoteDataSource.getLessonNoteById(1L).first()

        assertNotNull(note)
        assertEquals(1L, note?.id)
    }

    @Test
    fun insertLessonNote() = runTest {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)

        lessonNoteDataSource.upsertLessonNote(
            id = null,
            lessonId = 1L,
            title = "Title",
            text = "Text",
        )
        val actual = lessonNoteDataSource.getLessonNotesByLessonId(1L).first()

        assertEquals(1, actual.size)
        assertEquals(1L, actual.first().lessonId)
        assertEquals("Title", actual.first().title)
        assertEquals("Text", actual.first().text)
    }

    @Test
    fun updateLessonNote() = runTest {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenNote(lessonId = 1L)

        lessonNoteDataSource.upsertLessonNote(
            id = 1L,
            lessonId = 1L,
            title = "Updated Title",
            text = "Updated Text",
        )
        val actual = lessonNoteDataSource.getLessonNotesByLessonId(1L).first()

        assertEquals(1, actual.size)
        assertEquals(1L, actual.first().lessonId)
        assertEquals("Updated Title", actual.first().title)
        assertEquals("Updated Text", actual.first().text)
    }

    @Test
    fun deleteLessonNoteById() = runTest {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenNote(lessonId = 1L)

        lessonNoteDataSource.deleteLessonNoteById(1L)
        val actual = lessonNoteDataSource.getLessonNotesByLessonId(1L).first()

        assertEquals(0, actual.size)
    }

    private suspend fun givenNote(
        lessonId: Long,
        id: Long? = null,
        title: String = "Title",
        description: String = "",
    ) {
        lessonNoteDataSource.upsertLessonNote(
            id = id,
            lessonId = lessonId,
            title = title,
            text = description,
        )
    }

    private suspend fun givenLesson(
        schoolClassId: Long,
        id: Long? = null,
        name: String = "Name",
    ) {
        lessonDataSource.upsertLesson(id = id, schoolClassId = schoolClassId, name = name)
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