package com.example.teacher.feature.studentnote

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.studentnote.StudentNoteRepository
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.database.datasource.studentnote.StudentNoteDataSource
import com.example.teacher.core.domain.GenerateNoteTitleUseCase
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.studentnote.data.StudentNoteFormViewModel
import com.example.teacher.feature.studentnote.nav.StudentNoteNavigation
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
class StudentNoteFormScreenTest {

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

    @Inject
    lateinit var generateNoteTitleUseCase: GenerateNoteTitleUseCase

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun createNegativeNote() {
        val negativeMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.student_note_negative_type))
        val titleMatcher =
            hasText(rule.activity.getString(R.string.student_note_title), substring = true)
        val descriptionMatcher =
            hasText(rule.activity.getString(R.string.student_note_description), substring = true)
        val submitMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.student_note_add_note))
        val viewModel = ViewModelProvider(rule.activity)[StudentNoteFormViewModel::class.java]
        rule.setContent { StudentNoteFormScreen(viewModel) }

        rule.onRoot().printToLog("Student Note Form")

        rule.onNode(negativeMatcher).performClick()
        rule.onNode(titleMatcher).performTextInput("Title")
        rule.onNode(descriptionMatcher).performTextInput("Description")
        rule.onNode(submitMatcher).assertIsEnabled().performClick()
        viewModel.onDeleteStudentNote()
    }

    @Test
    fun createNeutralNote() {
        val negativeMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.student_note_neutral_type))
        val titleMatcher =
            hasText(rule.activity.getString(R.string.student_note_title), substring = true)
        val descriptionMatcher =
            hasText(rule.activity.getString(R.string.student_note_description), substring = true)
        val submitMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.student_note_add_note))
        val viewModel = ViewModelProvider(rule.activity)[StudentNoteFormViewModel::class.java]
        rule.setContent { StudentNoteFormScreen(viewModel) }

        rule.onRoot().printToLog("Student Note Form")

        rule.onNode(negativeMatcher).performClick()
        rule.onNode(titleMatcher).performTextInput("Title")
        rule.onNode(descriptionMatcher).performTextInput("Description")
        rule.onNode(submitMatcher).assertIsEnabled().performClick()
    }

    @Test
    fun editNote() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        givenStudent(studentDataSource, schoolClassId = 1L)
        givenStudentNote(studentNoteDataSource, studentId = 1L, isNegative = true)
        val submitMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.student_note_edit_note))
        val savedStateHandle = SavedStateHandle(
            mapOf(
                StudentNoteNavigation.studentIdArg to 1L,
                StudentNoteNavigation.studentNoteIdArg to 1L,
            )
        )
        val viewModel = StudentNoteFormViewModel(
            studentNoteRepository,
            savedStateHandle,
            generateNoteTitleUseCase,
        )
        rule.setContent { StudentNoteFormScreen(viewModel, isEditMode = true) }

        rule.waitUntil { viewModel.studentNoteResult.value is Result.Success }
        rule.onRoot().printToLog("Student Note Form")

        rule.onNode(submitMatcher).assertIsEnabled().performClick()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun StudentNoteFormScreen(
        viewModel: StudentNoteFormViewModel,
        isEditMode: Boolean = false,
    ) {
        val studentNoteResult by viewModel.studentNoteResult.collectAsStateWithLifecycle()
        val studentFullName by viewModel.studentFullName.collectAsStateWithLifecycle()
        val isStudentNoteDeleted by viewModel.isStudentNoteDeleted.collectAsStateWithLifecycle()
        val form by viewModel.form.collectAsStateWithLifecycle()
        StudentNoteFormScreen(
            studentNoteResult = studentNoteResult,
            snackbarHostState = remember { SnackbarHostState() },
            showNavigationIcon = false,
            onNavBack = {},
            onDeleteStudentNoteClick = {},
            formStatus = form.status,
            studentFullName = studentFullName.orEmpty(),
            title = form.title,
            onTitleChange = viewModel::onTitleChange,
            description = form.description,
            onDescriptionChange = viewModel::onDescriptionChange,
            isNoteNegative = form.isNegative,
            onIsNoteNegativeChange = viewModel::onIsNoteNegativeChange,
            isSubmitEnabled = form.isSubmitEnabled,
            onAddStudentNote = viewModel::onSubmit,
            isEditMode = isEditMode,
            isStudentNoteDeleted = isStudentNoteDeleted,
        )
    }
}