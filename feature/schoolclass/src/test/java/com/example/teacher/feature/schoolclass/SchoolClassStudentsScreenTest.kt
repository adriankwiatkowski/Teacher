package com.example.teacher.feature.schoolclass

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.BasicStudentsPreviewParameterProvider
import com.example.teacher.feature.schoolclass.data.SchoolClassViewModel
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
class SchoolClassStudentsScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var studentDataSource: StudentDataSource

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Inject
    lateinit var schoolClassRepository: SchoolClassRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun empty() {
        val viewModel = ViewModelProvider(rule.activity)[SchoolClassViewModel::class.java]
        rule.setContent {
            SchoolClassStudentsScreen(viewModel, emptyList())
        }
        rule.onRoot().printToLog("School Class Students")

        rule.onNodeWithText(rule.activity.getString(R.string.school_class_no_students_in_class))
            .assertExists()
    }

    @Test
    fun labelExists() {
        val viewModel = ViewModelProvider(rule.activity)[SchoolClassViewModel::class.java]
        val students = BasicStudentsPreviewParameterProvider().values.first()
        rule.setContent { SchoolClassStudentsScreen(viewModel, students) }
        rule.onRoot().printToLog("School Class Students")

        rule.onNodeWithText(students.first().fullName, substring = true).assertExists()
    }

    @Test
    fun pickRandomWorks() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L, name = "Name", surname = "Surname")
        val savedStateHandle = SavedStateHandle(mapOf(SchoolClassNavigation.schoolClassIdArg to 1L))
        val viewModel = SchoolClassViewModel(schoolClassRepository, savedStateHandle)
        val students = BasicStudentsPreviewParameterProvider().values.first()
        rule.setContent { SchoolClassStudentsScreen(viewModel, students) }

        rule.waitUntil { viewModel.schoolClassResult.value is Result.Success }
        rule.onRoot().printToLog("School Class Students")

        rule.onNodeWithText(rule.activity.getString(R.string.school_class_pick_random_student))
            .performClick()

        rule.onNodeWithText(rule.activity.getString(R.string.school_class_random_student_label))
            .assertExists()
        rule.onNodeWithText("Name Surname", substring = true).assertExists()
        viewModel.onDelete()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun SchoolClassStudentsScreen(
        viewModel: SchoolClassViewModel,
        students: List<BasicStudent>,
    ) {
        val randomStudent by viewModel.randomStudent.collectAsStateWithLifecycle()
        SchoolClassStudentsScreen(
            snackbarHostState = remember { SnackbarHostState() },
            students = students,
            randomStudent = randomStudent,
            pickRandomStudent = viewModel::pickRandomStudent,
            onStudentClick = {},
            onAddStudentClick = {},
        )
    }
}