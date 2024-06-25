package com.example.teacher.feature.lesson.note

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.note.data.NoteFormViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NoteFormScreenTest {

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
    fun inputWorks() {
        val viewModel = ViewModelProvider(rule.activity)[NoteFormViewModel::class.java]

        rule.setContent {
            val lessonNoteResult by viewModel.noteResult.collectAsStateWithLifecycle()
            val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
            val form by viewModel.form.collectAsStateWithLifecycle()
            NoteFormScreen(
                noteResult = lessonNoteResult,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = false,
                onNavBack = {},
                onDeleteNoteClick = {},
                formStatus = form.status,
                title = form.title,
                onTitleChange = viewModel::onTitleChange,
                text = form.text,
                onTextChange = viewModel::onTextChange,
                isSubmitEnabled = form.isSubmitEnabled,
                onAddNote = viewModel::onSubmit,
                isEditMode = false,
                isNoteDeleted = isDeleted,
            )
        }

        rule.onRoot().printToLog("Lesson Note Form")

        viewModel.onTitleChange("Title")
        viewModel.onTextChange("Text")

        rule.onNodeWithText(rule.activity.getString(R.string.lesson_title), substring = true)
            .assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_description), substring = true)
            .assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_add_note))
            .assertExists()
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()
    }
}