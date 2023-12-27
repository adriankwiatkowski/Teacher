package com.example.teacher.core.database.datasource.schoolyear

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class SchoolYearDataSourceTest {

    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getAllSchoolYears() = runTest {
        givenSchoolYear()

        val schoolYears = schoolYearDataSource.getAllSchoolYears().first()

        assertEquals(1, schoolYears.size)
    }

    @Test
    fun getSchoolYearById() = runTest {
        givenSchoolYear(name = "New Year")

        val schoolYear = schoolYearDataSource.getSchoolYearById(1L).first()

        assertNotNull(schoolYear)
        assertEquals("New Year", schoolYear?.name)
    }

    @Test
    fun insertSchoolYear() = runTest {
        givenSchoolYear(name = "New Year")

        val schoolYear = schoolYearDataSource.getSchoolYearById(1L).first()

        assertNotNull(schoolYear)
        assertEquals("New Year", schoolYear?.name)
    }

    @Test
    fun updateSchoolYear() = runTest {
        val inserted = givenSchoolYear(name = "New Year")

        val updated = givenSchoolYear(id = inserted.id, name = "Updated Year")

        assertEquals("Updated Year", updated.name)
    }

    @Test
    fun deleteSchoolYearById() = runTest {
        givenSchoolYear()

        schoolYearDataSource.deleteSchoolYearById(1L)
        val actual = schoolYearDataSource.getAllSchoolYears().first()

        assertEquals(0, actual.size)
    }

    private suspend fun givenSchoolYear(
        id: Long? = null,
        name: String = "School Year",
        firstTerm: Term = Term(
            1L,
            name = "Term I",
            startDate = TimeUtils.currentDate(),
            endDate = TimeUtils.plusDays(TimeUtils.currentDate(), 1)
        ),
        secondTerm: Term = Term(
            2L,
            name = "Term II",
            startDate = TimeUtils.plusDays(TimeUtils.currentDate(), 2),
            endDate = TimeUtils.plusDays(TimeUtils.currentDate(), 3),
        ),
    ): SchoolYear {
        schoolYearDataSource.insertOrUpdateSchoolYear(
            id = id,
            schoolYearName = name,
            termFirstName = firstTerm.name,
            termFirstStartDate = firstTerm.startDate,
            termFirstEndDate = firstTerm.endDate,
            termSecondName = secondTerm.name,
            termSecondStartDate = secondTerm.startDate,
            termSecondEndDate = secondTerm.endDate,
        )

        return schoolYearDataSource.getAllSchoolYears().first().last()
    }
}