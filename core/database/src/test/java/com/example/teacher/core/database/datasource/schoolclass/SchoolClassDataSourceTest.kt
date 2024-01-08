package com.example.teacher.core.database.datasource.schoolclass

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
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

class SchoolClassDataSourceTest {

    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getBasicSchoolClassById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())

        val actual = schoolClassDataSource.getBasicSchoolClassById(1L).first()

        assertNotNull(actual)
        assertEquals(1L, actual?.id)
    }

    @Test
    fun getSchoolClassById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())

        val actual = schoolClassDataSource.getSchoolClassById(1L).first()

        assertNotNull(actual)
        assertEquals(1L, actual?.id)
    }

    @Test
    fun getAllSchoolClasses() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())

        val actual = schoolClassDataSource.getAllSchoolClasses().first()

        assertEquals(1, actual.size)
    }

    @Test
    fun insertSchoolClass() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass(name = "Test School Class"))

        val actual = schoolClassDataSource.getSchoolClassById(1L).first()

        assertNotNull(actual)
        assertEquals(1L, actual?.id)
        assertEquals("Test School Class", actual?.name)
        assertEquals(0, actual?.students?.size)
        assertEquals(0, actual?.lessons?.size)
    }

    @Test
    fun updateSchoolClass() = runTest(testDispatcher) {
        val oldSchoolClass = testSchoolClass()
        givenSchoolClass(oldSchoolClass)

        givenSchoolClass(oldSchoolClass.copy(name = "Updated Name"), id = 1L)
        val actual = schoolClassDataSource.getSchoolClassById(1L).first()

        assertNotNull(actual)
        assertEquals(1L, actual?.id)
        assertEquals("Updated Name", actual?.name)
        assertEquals(0, actual?.students?.size)
        assertEquals(0, actual?.lessons?.size)
    }

    @Test
    fun deleteSchoolClassById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())

        schoolClassDataSource.deleteSchoolClassById(1L)
        val actual = schoolClassDataSource.getAllSchoolClasses().first()

        assertEquals(0, actual.size)
    }

    private suspend fun givenSchoolClass(basicSchoolClass: BasicSchoolClass, id: Long? = null) {
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
            id = id,
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