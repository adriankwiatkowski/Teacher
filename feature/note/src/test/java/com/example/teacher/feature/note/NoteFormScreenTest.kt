package com.example.teacher.feature.note

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.note.NoteRepository
import com.example.teacher.core.database.datasource.note.NoteDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.note.data.NoteFormViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NoteFormScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var noteRepository: NoteRepository

    @Inject
    lateinit var noteDataSource: NoteDataSource

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun priorityPickerExists() {
        val viewModel = ViewModelProvider(rule.activity)[NoteFormViewModel::class.java]
        val noteResultState = viewModel.noteResult
        val formState = viewModel.form

        rule.setContent {
            val form by formState.collectAsStateWithLifecycle()
            val noteResult by noteResultState.collectAsStateWithLifecycle()
            NoteFormScreen(
                noteResult = noteResult,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onDeleteNoteClick = viewModel::onDeleteNote,
                formStatus = form.status,
                title = form.title,
                onTitleChange = viewModel::onTitleChange,
                text = form.text,
                onTextChange = viewModel::onTextChange,
                priority = form.priority,
                onPriorityChange = viewModel::onPriorityChange,
                isSubmitEnabled = form.isSubmitEnabled,
                onAddNote = viewModel::onSubmit,
                isEditMode = false,
                isNoteDeleted = false,
            )
        }

        rule.waitUntil { noteResultState.value is Result.Success }

        rule.onRoot().printToLog("Note Form")
        rule.onNodeWithText(rule.activity.getString(R.string.note_note_priority))
            .assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.note_priority_low))
            .assertExists()
            .assertHasClickAction()
        rule.onNodeWithText(rule.activity.getString(R.string.note_priority_medium))
            .assertExists()
            .assertHasClickAction()
        rule.onNodeWithText(rule.activity.getString(R.string.note_priority_high))
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun create() = runTest {
        val viewModel = ViewModelProvider(rule.activity)[NoteFormViewModel::class.java]
        viewModel.onTitleChange("Title")
        viewModel.onTextChange("Text")
        val noteResultState = viewModel.noteResult
        val formState = viewModel.form

        rule.setContent {
            val form by formState.collectAsStateWithLifecycle()
            val noteResult by noteResultState.collectAsStateWithLifecycle()
            NoteFormScreen(
                noteResult = noteResult,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onDeleteNoteClick = viewModel::onDeleteNote,
                formStatus = form.status,
                title = form.title,
                onTitleChange = viewModel::onTitleChange,
                text = form.text,
                onTextChange = viewModel::onTextChange,
                priority = form.priority,
                onPriorityChange = viewModel::onPriorityChange,
                isSubmitEnabled = form.isSubmitEnabled,
                onAddNote = viewModel::onSubmit,
                isEditMode = false,
                isNoteDeleted = false,
            )
        }

        rule.onRoot().printToLog("Note Form")
        rule.onNodeWithText("Title").apply {
            performTextInput(" Turbo")
        }
        rule.onNodeWithText("Text").apply {
            performTextInput(" Turbo")
        }
        rule.onNodeWithText(rule.activity.getString(R.string.note_add_note))
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()

        formState.value.status is FormStatus.Saving

        noteDataSource.getNotes().test {
            val notes = awaitItem().ifEmpty { awaitItem() }
            assertEquals(1, notes.size)

            assertEquals(FormStatus.Success, formState.value.status)
        }
    }
}