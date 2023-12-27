package com.example.teacher.core.database.datasource.gradetemplate

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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GradeTemplateDataSourceTest {

    private lateinit var gradeTemplateDataSource: GradeTemplateDataSource
    private lateinit var lessonDataSource: LessonDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        gradeTemplateDataSource = GradeTemplateDataSourceImpl(db, testDispatcher)
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getGradeTemplateById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)

        val gradeTemplate = gradeTemplateDataSource.getGradeTemplateById(1L).first()

        assertNotNull(gradeTemplate)
    }

    @Test
    fun getGradeTemplatesByLessonId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)
        givenGradeTemplate(lessonId = 1L)

        val gradeTemplates = gradeTemplateDataSource.getGradeTemplatesByLessonId(1L).first()

        assertEquals(2, gradeTemplates.size)
    }

    @Test
    fun insertGradeTemplate() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)

        givenGradeTemplate(
            lessonId = 1L,
            name = "New Grade Template",
            description = "Description",
            weight = 5,
            isFirstTerm = true,
        )
        val gradeTemplate = gradeTemplateDataSource.getGradeTemplateById(1L).first()

        assertNotNull(gradeTemplate)
        assertEquals("New Grade Template", gradeTemplate?.name)
        assertEquals("Description", gradeTemplate?.description)
        assertEquals(5, gradeTemplate?.weight)
        assertEquals(true, gradeTemplate?.isFirstTerm)
    }

    @Test
    fun updateGradeTemplate() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)

        givenGradeTemplate(
            id = 1L,
            lessonId = 1L,
            name = "Updated Grade Template",
            description = "Updated Description",
            weight = 3,
            isFirstTerm = false,
        )
        val gradeTemplate = gradeTemplateDataSource.getGradeTemplateById(1L).first()

        assertNotNull(gradeTemplate)
        assertEquals("Updated Grade Template", gradeTemplate?.name)
        assertEquals("Updated Description", gradeTemplate?.description)
        assertEquals(3, gradeTemplate?.weight)
        assertEquals(false, gradeTemplate?.isFirstTerm)
    }

    @Test
    fun deleteGradeTemplateById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)

        gradeTemplateDataSource.deleteGradeTemplateById(1L)
        val gradeTemplate = gradeTemplateDataSource.getGradeTemplateById(1L).first()

        assertNull(gradeTemplate)
    }

    private suspend fun givenGradeTemplate(
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

    private suspend fun givenLesson(
        schoolClassId: Long,
        id: Long? = null,
        name: String = "Name",
    ) {
        lessonDataSource.insertOrUpdateLesson(id = id, schoolClassId = schoolClassId, name = name)
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