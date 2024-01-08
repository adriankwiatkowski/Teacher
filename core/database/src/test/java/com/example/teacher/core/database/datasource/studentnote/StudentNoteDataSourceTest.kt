package com.example.teacher.core.database.datasource.studentnote

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSourceImpl
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSourceImpl
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSourceImpl
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

class StudentNoteDataSourceTest {

    private lateinit var studentNoteDataSource: StudentNoteDataSource
    private lateinit var studentDataSource: StudentDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        studentNoteDataSource = StudentNoteDataSourceImpl(db, testDispatcher)
        studentDataSource = StudentDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getStudentNotesByStudentId() = runTest {
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenNote(studentId = 1L)

        val notes = studentNoteDataSource.getStudentNotesByStudentId(1L).first()

        assertEquals(1, notes.size)
    }

    @Test
    fun getStudentNoteById() = runTest {
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenNote(studentId = 1L)

        val note = studentNoteDataSource.getStudentNoteById(1L).first()

        assertNotNull(note)
        assertEquals(1L, note?.id)
    }

    @Test
    fun getStudentFullNameNameById() = runTest {
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L, name = "Name", surname = "Surname")

        val actual = studentNoteDataSource.getStudentFullNameNameById(1L).first()

        assertNotNull(actual)
        assertEquals("Name Surname", actual)
    }

    @Test
    fun getStudentFullNameNameByNonExistentId() = runTest {
        val actual = studentNoteDataSource.getStudentFullNameNameById(0L).first()

        assertNull(actual)
    }

    @Test
    fun insertStudentNote() = runTest {
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)

        studentNoteDataSource.upsertStudentNote(
            id = null,
            studentId = 1L,
            title = "Title",
            description = "",
            isNegative = false,
        )
        val actual = studentNoteDataSource.getStudentNotesByStudentId(1L).first()

        assertEquals(1, actual.size)
        assertEquals(1L, actual.first().studentId)
        assertEquals("Title", actual.first().title)
        assertEquals(false, actual.first().isNegative)
    }

    @Test
    fun updateStudentNote() = runTest {
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenNote(studentId = 1L)

        studentNoteDataSource.upsertStudentNote(
            id = 1L,
            studentId = 1L,
            title = "Updated Title",
            description = "",
            isNegative = false,
        )
        val actual = studentNoteDataSource.getStudentNotesByStudentId(1L).first()

        assertEquals(1, actual.size)
        assertEquals(1L, actual.first().studentId)
        assertEquals("Updated Title", actual.first().title)
    }

    @Test
    fun deleteStudentNoteById() = runTest {
        givenSchoolClass(testSchoolClass())
        givenStudent(schoolClassId = 1L)
        givenNote(studentId = 1L)

        studentNoteDataSource.deleteStudentNoteById(1L)
        val actual = studentNoteDataSource.getStudentNotesByStudentId(1L).first()

        assertEquals(0, actual.size)
    }

    private suspend fun givenNote(
        studentId: Long,
        id: Long? = null,
        title: String = "Title",
        description: String = "",
        isNegative: Boolean = false,
    ) {
        studentNoteDataSource.upsertStudentNote(
            id = id,
            studentId = studentId,
            title = title,
            description = description,
            isNegative = isNegative,
        )
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