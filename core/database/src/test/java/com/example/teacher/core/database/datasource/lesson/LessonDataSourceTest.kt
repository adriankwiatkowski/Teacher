package com.example.teacher.core.database.datasource.lesson

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
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

class LessonDataSourceTest {

    private lateinit var lessonDataSource: LessonDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getLessons() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)

        val lessons = lessonDataSource.getLessons().first()

        assertEquals(2, lessons.size)
    }

    @Test
    fun getLessonsByYear() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)

        val lessonsByYear = lessonDataSource.getLessonsByYear().first()

        assertEquals(1, lessonsByYear.size)
        assertEquals(1, lessonsByYear.first().lessonsBySchoolClass.size)
        assertEquals(2, lessonsByYear.first().lessonsBySchoolClass.first().lessons.size)
    }

    @Test
    fun getLessonById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)

        val lesson = lessonDataSource.getLessonById(1L).first()

        assertNotNull(lesson)
        assertEquals(1L, lesson?.id)
    }

    @Test
    fun getLessonsBySchoolClassId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenLesson(schoolClassId = 1L)
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 2L)

        val lessons = lessonDataSource.getLessonsBySchoolClassId(1L).first()

        assertEquals(2, lessons.size)
    }

    @Test
    fun getStudentSchoolClassNameById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass(name = "School Class"))

        val schoolClassName = lessonDataSource.getStudentSchoolClassNameById(1L).first()

        assertNotNull(schoolClassName)
        assertEquals("School Class", schoolClassName)
    }

    @Test
    fun getSchoolYearByLessonId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)

        val schoolYear = lessonDataSource.getSchoolYearByLessonId(1L).first()

        assertNotNull(schoolYear)
        assertEquals(1L, schoolYear?.id)
    }

    @Test
    fun insertLesson() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())

        givenLesson(schoolClassId = 1L, name = "Mathematics")
        val lesson = lessonDataSource.getLessons().first().firstOrNull()

        assertNotNull(lesson)
        assertEquals("Mathematics", lesson?.name)
    }

    @Test
    fun updateLesson() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L, name = "Mathematics")

        givenLesson(schoolClassId = 1L, id = 1L, name = "Physics")
        val lesson = lessonDataSource.getLessons().first().firstOrNull()

        assertNotNull(lesson)
        assertEquals("Physics", lesson?.name)
    }

    @Test
    fun deleteLessonById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)

        lessonDataSource.deleteLessonById(1L)
        val lessons = lessonDataSource.getLessons().first()

        assertEquals(0, lessons.size)
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