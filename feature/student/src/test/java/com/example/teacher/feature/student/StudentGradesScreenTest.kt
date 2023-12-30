package com.example.teacher.feature.student

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.data.repository.student.StudentRepository
import com.example.teacher.core.database.datasource.grade.GradeDataSource
import com.example.teacher.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.StudentPreviewParameterProvider
import com.example.teacher.feature.student.data.StudentGradesViewModel
import com.example.teacher.feature.student.nav.StudentNavigation
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class StudentGradesScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var studentDataSource: StudentDataSource

    @Inject
    lateinit var gradeDataSource: GradeDataSource

    @Inject
    lateinit var gradeTemplateDataSource: GradeTemplateDataSource

    @Inject
    lateinit var lessonDataSource: LessonDataSource

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Inject
    lateinit var studentRepository: StudentRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun empty() {
        val savedStateHandle = SavedStateHandle()
        val viewModel = StudentGradesViewModel(studentRepository, savedStateHandle)
        val student = StudentPreviewParameterProvider().values.first()
        rule.setContent { StudentNotesScreen(viewModel, student) }
        rule.onRoot().printToLog("Student Grades")

        rule.waitUntil { viewModel.studentGradesResult.value is Result.Success }
        rule.onNodeWithText(rule.activity.getString(R.string.student_empty_grades)).assertExists()
    }

    @Test
    fun list() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L, name = "Mathematics")
        givenGradeTemplate(gradeTemplateDataSource, lessonId = 1L)
        givenGrade(gradeDataSource, gradeTemplateId = 1L, studentId = 1L)
        val savedStateHandle = SavedStateHandle(mapOf(StudentNavigation.studentIdArg to 1L))
        val viewModel = StudentGradesViewModel(studentRepository, savedStateHandle)
        val student = studentDataSource.getStudentById(1L).first()!!
        rule.setContent { StudentNotesScreen(viewModel, student) }
        rule.onRoot().printToLog("Student Grades")

        rule.waitUntil { viewModel.studentGradesResult.value is Result.Success }

        rule.onNodeWithText("Mathematics").assertExists()
    }

    @Test
    fun gradeDialog() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenLesson(lessonDataSource, schoolClassId = 1L, name = "Mathematics")
        givenGradeTemplate(gradeTemplateDataSource, lessonId = 1L)
        givenGrade(gradeDataSource, gradeTemplateId = 1L, studentId = 1L, grade = DecimalUtils.Six)
        val savedStateHandle = SavedStateHandle(mapOf(StudentNavigation.studentIdArg to 1L))
        val viewModel = StudentGradesViewModel(studentRepository, savedStateHandle)
        val student = studentDataSource.getStudentById(1L).first()!!
        rule.setContent { StudentNotesScreen(viewModel, student) }
        rule.onRoot().printToLog("Student Grades")

        rule.waitUntil { viewModel.studentGradesResult.value is Result.Success }

        rule.onNodeWithText("Mathematics").assertExists()
        rule.onNodeWithText("6").performClick()
        rule.onNodeWithText(student.fullName).assertExists()
        rule.onNodeWithText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_ok))
            .performClick()
        rule.onNodeWithText(student.fullName).assertDoesNotExist()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun StudentNotesScreen(viewModel: StudentGradesViewModel, student: Student) {
        val studentGradesResult by viewModel.studentGradesResult.collectAsStateWithLifecycle()
        val gradeDialog by viewModel.gradeDialog.collectAsStateWithLifecycle()
        StudentGradesScreen(
            studentGradesResult = studentGradesResult,
            snackbarHostState = remember { SnackbarHostState() },
            gradeDialog = gradeDialog,
            onShowGradeDialog = { gradeInfo, grade ->
                viewModel.onShowGradeDialog(
                    student = student,
                    gradeInfo = gradeInfo,
                    grade = grade,
                )
            },
            onGradeDialogDismiss = viewModel::onDismissGradeDialog,
        )
    }
}