package com.example.teacher.feature.student

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.studentnote.StudentNoteRepository
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.database.datasource.studentnote.StudentNoteDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.student.data.StudentNotesViewModel
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
class StudentNotesScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var studentNoteDataSource: StudentNoteDataSource

    @Inject
    lateinit var studentDataSource: StudentDataSource

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Inject
    lateinit var studentNoteRepository: StudentNoteRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun empty() {
        val savedStateHandle = SavedStateHandle()
        val viewModel = StudentNotesViewModel(studentNoteRepository, savedStateHandle)
        rule.setContent { StudentNotesScreen(viewModel) }
        rule.onRoot().printToLog("Student Notes")

        rule.waitUntil { viewModel.studentNotesResult.value is Result.Success }
        rule.onNodeWithText(rule.activity.getString(R.string.student_empty_notes)).assertExists()
    }

    @Test
    fun list() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenStudentNote(studentNoteDataSource, studentId = 1L, isNegative = true)
        givenStudentNote(studentNoteDataSource, studentId = 1L, isNegative = false)
        val savedStateHandle = SavedStateHandle(mapOf(StudentNavigation.studentIdArg to 1L))
        val viewModel = StudentNotesViewModel(studentNoteRepository, savedStateHandle)
        rule.setContent { StudentNotesScreen(viewModel) }
        rule.onRoot().printToLog("Student Notes")

        rule.waitUntil { viewModel.studentNotesResult.value is Result.Success }

        rule.onNodeWithText(rule.activity.getString(R.string.student_notes_neutral)).assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.student_notes_negative)).assertExists()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun StudentNotesScreen(viewModel: StudentNotesViewModel) {
        val studentNotesResult by viewModel.studentNotesResult.collectAsStateWithLifecycle()
        StudentNotesScreen(
            studentNotesResult = studentNotesResult,
            snackbarHostState = remember { SnackbarHostState() },
            onNoteClick = {},
            onAddNoteClick = {},
        )
    }
}