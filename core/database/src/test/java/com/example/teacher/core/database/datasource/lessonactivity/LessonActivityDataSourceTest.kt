package com.example.teacher.core.database.datasource.lessonactivity

import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSourceImpl
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
import org.junit.Before
import org.junit.Test

class LessonActivityDataSourceTest {

    private lateinit var lessonActivityDataSource: LessonActivityDataSource
    private lateinit var lessonDataSource: LessonDataSource
    private lateinit var studentDataSource: StudentDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        lessonActivityDataSource = LessonActivityDataSourceImpl(db, testDispatcher)
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
        studentDataSource = StudentDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getLessonActivitiesByLessonId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        val students = 4
        for (i in 0..<students) {
            givenStudent(schoolClassId = 1L)
        }

        val activities = lessonActivityDataSource.getLessonActivitiesByLessonId(1L).first()

        assertEquals(students * 2, activities.size)
    }

    @Test
    fun insertLessonActivity() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)

        lessonActivityDataSource.upsertLessonActivity(
            id = null,
            lessonId = 1L,
            studentId = 1L,
            sum = 10,
            isFirstTerm = true,
        )
        val activity = lessonActivityDataSource.getLessonActivitiesByLessonId(1L).first()
            .firstOrNull { it.isFirstTerm }

        assertNotNull(activity)
        assertEquals(10L, activity?.sum)
    }

    @Test
    fun updateLessonActivity() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenLessonActivity(lessonId = 1L, studentId = 1L, sum = 10)

        lessonActivityDataSource.upsertLessonActivity(
            id = 1L,
            lessonId = 1L,
            studentId = 1L,
            sum = 20,
            isFirstTerm = true,
        )
        val activity = lessonActivityDataSource.getLessonActivitiesByLessonId(1L).first()
            .firstOrNull { it.isFirstTerm }

        assertNotNull(activity)
        assertEquals(20L, activity?.sum)
    }

    private suspend fun givenLessonActivity(
        lessonId: Long,
        studentId: Long,
        id: Long? = null,
        sum: Long = 0,
        isFirstTerm: Boolean = true,
    ) {
        lessonActivityDataSource.upsertLessonActivity(
            id = id,
            lessonId = lessonId,
            studentId = studentId,
            sum = sum,
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