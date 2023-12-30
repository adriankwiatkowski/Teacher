package com.example.teacher.feature.student

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.student.StudentRepository
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.student.data.StudentFormViewModel
import com.example.teacher.feature.student.nav.StudentNavigation
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
class StudentFormScreenTest {

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
    lateinit var studentRepository: StudentRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun loading() {
        val savedStateHandle = SavedStateHandle()
        val viewModel = StudentFormViewModel(studentRepository, savedStateHandle)
        rule.setContent {
            val form by viewModel.form.collectAsStateWithLifecycle()
            StudentFormScreen(
                studentResult = Result.Loading,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = false,
                onNavBack = {},
                schoolClassName = "",
                formStatus = FormStatus.Idle,
                name = form.name,
                onNameChange = viewModel::onNameChange,
                surname = form.surname,
                onSurnameChange = viewModel::onSurnameChange,
                email = form.email,
                onEmailChange = viewModel::onEmailChange,
                phone = form.phone,
                onPhoneChange = viewModel::onPhoneChange,
                registerNumber = form.registerNumber,
                onRegisterNumberChange = viewModel::onRegisterNumberChange,
                isSubmitEnabled = form.isSubmitEnabled,
                onAddStudent = viewModel::onSubmit,
            )
        }

        rule.onRoot().printToLog("Student Form")

        rule.onNodeWithText(rule.activity.getString(com.example.teacher.core.ui.R.string.ui_loading))
            .assertExists()
    }

    @Test
    fun validInput_created() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        val nameInput = hasText("${rule.activity.getString(R.string.student_name)}*")
        val surnameInput = hasText("${rule.activity.getString(R.string.student_surname)}*")
        val emailInput = hasText(rule.activity.getString(R.string.student_email))
        val phoneInput = hasText(rule.activity.getString(R.string.student_phone))
        val registerNumberInput = hasText(rule.activity.getString(R.string.student_register_number))
        val submitButton =
            hasClickAction() and hasText(rule.activity.getString(R.string.add_student))

        val savedStateHandle = SavedStateHandle(mapOf(StudentNavigation.schoolClassIdArg to 1L))
        val viewModel = StudentFormViewModel(studentRepository, savedStateHandle)
        rule.setContent { StudentFormScreen(viewModel) }

        rule.onRoot().printToLog("Student Form")
        rule.waitUntil { viewModel.studentResult.value is Result.Success }

        studentDataSource.getStudentById(1L).test {
            assertNull(awaitItem())

            rule.onNode(nameInput).performTextInput("Jan")
            rule.onNode(surnameInput).performTextInput("Kowalski")
            rule.onNode(emailInput).performTextInput("jan.kowalski@email.com")
            rule.onNode(phoneInput).performTextInput("123456789")
            rule.onNode(registerNumberInput).performTextClearance()
            rule.onNode(submitButton).assertIsEnabled().performClick()

            assertNotNull(awaitItem())
        }
    }

    @Test
    fun update_happyPath() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L, name = "Name", surname = "Surname")
        val nameInput = hasText("${rule.activity.getString(R.string.student_name)}*")
        val surnameInput = hasText("${rule.activity.getString(R.string.student_surname)}*")
        val submitButton =
            hasClickAction() and hasText(rule.activity.getString(R.string.edit_student))

        val savedStateHandle = SavedStateHandle(
            mapOf(
                StudentNavigation.schoolClassIdArg to 1L,
                StudentNavigation.studentIdArg to 1L,
            )
        )
        val viewModel = StudentFormViewModel(studentRepository, savedStateHandle)
        rule.setContent { StudentFormScreen(viewModel) }

        rule.onRoot().printToLog("Student Form")
        rule.waitUntil { viewModel.studentResult.value is Result.Success }

        studentDataSource.getStudentById(1L).test {
            assertNotNull(awaitItem())

            rule.onNode(nameInput).performTextClearance()
            rule.onNode(nameInput).performTextInput("Kowal")
            rule.onNode(surnameInput).performTextClearance()
            rule.onNode(surnameInput).performTextInput("Nowak")
            rule.onNode(submitButton).assertIsEnabled().performClick()

            val updatedStudent = awaitItem()
            assertNotNull(updatedStudent)
            assertEquals("Kowal", updatedStudent?.name)
            assertEquals("Nowak", updatedStudent?.surname)
        }
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun StudentFormScreen(viewModel: StudentFormViewModel) {
        val studentResult by viewModel.studentResult.collectAsStateWithLifecycle()
        val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()
        val form by viewModel.form.collectAsStateWithLifecycle()
        StudentFormScreen(
            studentResult = studentResult,
            snackbarHostState = remember { SnackbarHostState() },
            showNavigationIcon = false,
            onNavBack = {},
            schoolClassName = schoolClassName.orEmpty(),
            formStatus = form.status,
            name = form.name,
            onNameChange = viewModel::onNameChange,
            surname = form.surname,
            onSurnameChange = viewModel::onSurnameChange,
            email = form.email,
            onEmailChange = viewModel::onEmailChange,
            phone = form.phone,
            onPhoneChange = viewModel::onPhoneChange,
            registerNumber = form.registerNumber,
            onRegisterNumberChange = viewModel::onRegisterNumberChange,
            isSubmitEnabled = form.isSubmitEnabled,
            onAddStudent = viewModel::onSubmit,
        )
    }
}