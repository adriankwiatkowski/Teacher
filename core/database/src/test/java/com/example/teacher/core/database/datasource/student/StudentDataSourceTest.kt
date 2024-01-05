package com.example.teacher.core.database.datasource.student

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSourceImpl
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSourceImpl
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term
import com.example.teacher.core.model.data.toBasic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class StudentDataSourceTest {

    private lateinit var studentDataSource: StudentDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()

        studentDataSource = StudentDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun studentDataSource_insertWithoutSchoolClass_fails() = runTest {
        val student = testStudent()

        studentDataSource.insertOrUpdateStudent(
            id = null,
            schoolClassId = student.schoolClassId,
            registerNumber = student.registerNumber,
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )

        val studentById = studentDataSource.getStudentById(1L).first()
        val studentsBySchoolClass = studentDataSource.getStudentsBySchoolClassId(1L).first()

        assertNull(studentById)
        assertEquals(emptyList<BasicStudent>(), studentsBySchoolClass)
    }

    @Test
    fun studentDataSource_insertWithSchoolClass_ok() = runTest {
        insertSchoolClass(testSchoolClass())
        val student = testStudent(id = 1L, registerNumber = 3L, classId = 1L)

        studentDataSource.insertOrUpdateStudent(
            id = null,
            schoolClassId = student.schoolClassId,
            registerNumber = student.registerNumber,
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )

        val savedBasicStudent = studentDataSource.getBasicStudentById(1L).first()
        val savedStudent = studentDataSource.getStudentById(1L).first()?.toBasic()

        assertEquals(student, savedBasicStudent)
        assertEquals(student, savedStudent)
    }

    @Test
    fun studentDataSource_insertStudent_usedRegisterNumbersIsNotEmpty() = runTest {
        insertSchoolClass(testSchoolClass())
        val student = testStudent()

        studentDataSource.insertOrUpdateStudent(
            id = null,
            schoolClassId = student.schoolClassId,
            registerNumber = student.registerNumber,
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )

        val usedRegisterNumbers =
            studentDataSource.getUsedRegisterNumbersBySchoolClassId(1L).first()

        assertEquals(1, usedRegisterNumbers.size)
    }

    @Test
    fun studentDataSource_updateStudent_happyPath() = runTest {
        insertSchoolClass(testSchoolClass())
        val student = testStudent()
        studentDataSource.insertOrUpdateStudent(
            id = null,
            schoolClassId = student.schoolClassId,
            registerNumber = student.registerNumber,
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )

        studentDataSource.insertOrUpdateStudent(
            id = 1L,
            schoolClassId = student.schoolClassId,
            registerNumber = student.registerNumber,
            name = "Updated Name",
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )

        val actual = studentDataSource.getStudentById(1L).first()

        assertNotNull(actual)
        assertEquals("Updated Name", actual?.name)
    }

    @Test
    fun studentDataSource_fetchesItemsByRegisterNumber() = runTest {
        insertSchoolClass(testSchoolClass())
        val students = listOf(
            testStudent(id = 1L, registerNumber = 3L, classId = 1L),
            testStudent(id = 2L, registerNumber = 2L, classId = 1L),
            testStudent(id = 3L, registerNumber = 1L, classId = 1L),
        )

        for (student in students) {
            studentDataSource.insertOrUpdateStudent(
                id = null,
                schoolClassId = student.schoolClassId,
                registerNumber = student.registerNumber,
                name = student.name,
                surname = student.surname,
                email = student.email,
                phone = student.phone,
            )
        }

        val savedStudents = studentDataSource.getStudentsBySchoolClassId(1L).first()

        assertEquals(
            listOf(1L, 2L, 3L),
            savedStudents.map(BasicStudent::registerNumber)
        )
    }

    @Test
    fun studentDataSource_deleteStudentById_ok() = runTest {
        insertSchoolClass(testSchoolClass())
        val student = testStudent()

        studentDataSource.insertOrUpdateStudent(
            id = null,
            schoolClassId = student.schoolClassId,
            registerNumber = student.registerNumber,
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )

        val studentsBeforeDeletion = studentDataSource.getStudentsBySchoolClassId(1L).first()
        studentDataSource.deleteStudentById(1L)
        val studentsAfterDeletion = studentDataSource.getStudentsBySchoolClassId(1L).first()

        assertEquals(1, studentsBeforeDeletion.size)
        assertEquals(0, studentsAfterDeletion.size)
    }

    @Test
    fun studentDataSource_deleteStudentByNonExistentId_ok() = runTest {
        insertSchoolClass(testSchoolClass())
        val student = testStudent()

        studentDataSource.insertOrUpdateStudent(
            id = null,
            schoolClassId = student.schoolClassId,
            registerNumber = student.registerNumber,
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
        )

        val studentsBeforeDeletion = studentDataSource.getStudentsBySchoolClassId(1L).first()
        studentDataSource.deleteStudentById(2L)
        val studentsAfterDeletion = studentDataSource.getStudentsBySchoolClassId(1L).first()

        assertEquals(1, studentsBeforeDeletion.size)
        assertEquals(1, studentsAfterDeletion.size)
    }

    private suspend fun insertSchoolClass(basicSchoolClass: BasicSchoolClass) {
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

private fun testStudent(
    id: Long = 0,
    name: String = "Name",
    surname: String = "Surname",
    registerNumber: Long = id,
    email: String? = null,
    phone: String? = null,
    classId: Long = 1,
) = BasicStudent(
    id = id,
    name = name,
    surname = surname,
    registerNumber = registerNumber,
    email = email,
    phone = phone,
    schoolClassId = classId,
)