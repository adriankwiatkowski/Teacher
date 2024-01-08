package com.example.teacher.core.database.datasource.gradescore

import app.cash.turbine.test
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacher.core.database.datasource.gradetemplate.GradeTemplateDataSourceImpl
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
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GradeScoreDataSourceTest {

    private lateinit var gradeScoreDataSource: GradeScoreDataSource
    private lateinit var gradeTemplateDataSource: GradeTemplateDataSource
    private lateinit var lessonDataSource: LessonDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        gradeScoreDataSource = GradeScoreDataSourceImpl(db, testDispatcher)
        gradeTemplateDataSource = GradeTemplateDataSourceImpl(db, testDispatcher)
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getGradeScoreByGradeTemplateId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)

        gradeScoreDataSource.getGradeScoreByGradeTemplateId(1L)
            .test {
                val initial = awaitItem()
                if (initial == null) {
                    assertNotNull(awaitItem())
                }
            }
    }

    @Test
    fun updateGradeScore() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)

        gradeScoreDataSource.getGradeScoreByGradeTemplateId(1L)
            .test {
                val gradeScore = awaitItem() ?: let {
                    val gradeScore = awaitItem()
                    assertNotNull(gradeScore)
                    gradeScore!!
                }

                gradeScoreDataSource.updateGradeScore(gradeScore.copy(twoMinThreshold = 1L))

                val updated = awaitItem()
                assertEquals(1L, updated?.twoMinThreshold)
            }
    }

    private suspend fun givenGradeTemplate(
        lessonId: Long,
        id: Long? = null,
        name: String = "Grade Template",
        description: String? = null,
        weight: Int = 3,
        isFirstTerm: Boolean = true,
    ) {
        gradeTemplateDataSource.upsertGradeTemplate(
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