package com.example.teacher.feature.student

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.StudentPreviewParameterProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class StudentDetailScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun basicInfoExists() {
        val student = StudentPreviewParameterProvider().values.first()
        rule.setContent { StudentDetailScreen(student) }
        rule.onRoot().printToLog("Student Detail")

        rule.onNodeWithText(student.fullName).assertExists()
        rule.onNodeWithText(
            rule.activity.getString(
                R.string.student_register_number_with_data,
                student.registerNumber
            )
        ).assertExists()
    }

    @Test
    fun givenEmail_emailExists() {
        val student =
            StudentPreviewParameterProvider().values.first().copy(email = "email@email.com")
        rule.setContent { StudentDetailScreen(student) }
        rule.onRoot().printToLog("Student Detail")

        rule.onNodeWithText(rule.activity.getString(R.string.student_email)).assertExists()
        rule.onNodeWithText(student.email!!).assertExists()
    }

    @Test
    fun emailDoesNotExists() {
        val student = StudentPreviewParameterProvider().values.first().copy(email = null)
        rule.setContent { StudentDetailScreen(student) }
        rule.onRoot().printToLog("Student Detail")

        rule.onNodeWithText(rule.activity.getString(R.string.student_email)).assertDoesNotExist()
    }

    @Test
    fun givenPhone_phoneExists() {
        val student = StudentPreviewParameterProvider().values.first().copy(phone = "123123123")
        rule.setContent { StudentDetailScreen(student) }
        rule.onRoot().printToLog("Student Detail")

        rule.onNodeWithText(student.phone!!).assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.student_phone)).assertExists()
    }

    @Test
    fun phoneDoesNotExists() {
        val student = StudentPreviewParameterProvider().values.first().copy(phone = null)
        rule.setContent { StudentDetailScreen(student) }
        rule.onRoot().printToLog("Student Detail")

        rule.onNodeWithText(rule.activity.getString(R.string.student_phone)).assertDoesNotExist()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun StudentDetailScreen(student: Student) {
        StudentDetailScreen(
            student = student,
            snackbarHostState = remember { SnackbarHostState() },
            onEmailClick = {},
            onPhoneClick = {},
        )
    }
}