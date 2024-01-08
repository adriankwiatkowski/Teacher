package com.example.teacher.core.database.datasource.grade

import com.example.teacher.core.common.utils.DecimalUtils
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
import java.math.BigDecimal

class GradeDataSourceTest {

    private lateinit var gradeDataSource: GradeDataSource
    private lateinit var gradeTemplateDataSource: GradeTemplateDataSource
    private lateinit var lessonDataSource: LessonDataSource
    private lateinit var studentDataSource: StudentDataSource
    private lateinit var schoolClassDataSource: SchoolClassDataSource
    private lateinit var schoolYearDataSource: SchoolYearDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        gradeDataSource = GradeDataSourceImpl(db, testDispatcher)
        gradeTemplateDataSource = GradeTemplateDataSourceImpl(db, testDispatcher)
        lessonDataSource = LessonDataSourceImpl(db, testDispatcher)
        studentDataSource = StudentDataSourceImpl(db, testDispatcher)
        schoolClassDataSource = SchoolClassDataSourceImpl(db, testDispatcher)
        schoolYearDataSource = SchoolYearDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getGradeById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)
        givenGrade(gradeTemplateId = 1L, studentId = 1L)

        val grade = gradeDataSource.getGradeById(1L).first()

        assertNotNull(grade)
        assertEquals(1L, grade?.id)
    }

    @Test
    fun getGradesByGradeTemplateId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)
        givenGrade(gradeTemplateId = 1L, studentId = 1L, grade = DecimalUtils.Five)

        val grades = gradeDataSource.getGradesByGradeTemplateId(1L).first()
        val firstStudent = grades.firstOrNull { it.studentId == 1L }
        val secondStudent = grades.firstOrNull { it.studentId != 1L }

        assertEquals(2, grades.size)
        assertEquals(DecimalUtils.Five, firstStudent?.grade)
        assertEquals(null, secondStudent?.grade)
    }

    @Test
    fun getGradeTemplateInfoByGradeTemplateId() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)

        val gradeTemplateInfo = gradeDataSource.getGradeTemplateInfoByGradeTemplateId(1L).first()

        assertNotNull(gradeTemplateInfo)
    }

    @Test
    fun insertGrade() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)

        givenGrade(gradeTemplateId = 1L, studentId = 1L, grade = DecimalUtils.Six)
        val grade = gradeDataSource.getGradeById(1L).first()

        assertNotNull(grade)
        assertEquals(1L, grade?.id)
        assertEquals(1L, grade?.studentId)
        assertEquals(1L, grade?.gradeTemplate?.id)
        assertEquals(DecimalUtils.Six, grade?.grade)
    }

    @Test
    fun updateGrade() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)
        givenGrade(gradeTemplateId = 1L, studentId = 1L, grade = DecimalUtils.Six)

        givenGrade(id = 1L, gradeTemplateId = 1L, studentId = 1L, grade = DecimalUtils.Four)
        val grade = gradeDataSource.getGradeById(1L).first()

        assertNotNull(grade)
        assertEquals(1L, grade?.id)
        assertEquals(1L, grade?.studentId)
        assertEquals(1L, grade?.gradeTemplate?.id)
        assertEquals(DecimalUtils.Four, grade?.grade)
    }

    @Test
    fun deleteGradeById() = runTest(testDispatcher) {
        givenSchoolClass(testSchoolClass())
        givenLesson(schoolClassId = 1L)
        givenStudent(schoolClassId = 1L)
        givenGradeTemplate(lessonId = 1L)
        givenGrade(gradeTemplateId = 1L, studentId = 1L, grade = DecimalUtils.Six)

        gradeDataSource.deleteGradeById(1L)
        val grade = gradeDataSource.getGradeById(1L).first()

        assertNull(grade)
    }

    private suspend fun givenGrade(
        gradeTemplateId: Long,
        studentId: Long,
        id: Long? = null,
        grade: BigDecimal = DecimalUtils.Five,
    ) {
        gradeDataSource.upsertGrade(
            id = id,
            gradeTemplateId = gradeTemplateId,
            studentId = studentId,
            grade = grade,
        )
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