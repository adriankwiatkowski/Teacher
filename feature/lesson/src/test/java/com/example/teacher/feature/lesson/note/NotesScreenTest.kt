package com.example.teacher.feature.lesson.note

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.LessonNotesPreviewParameterProvider
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.note.data.NotesViewModel
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
class NotesScreenTest {

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
    fun empty() {
        val viewModel = ViewModelProvider(rule.activity)[NotesViewModel::class.java]

        rule.setContent {
            val notesResult by viewModel.lessonNotesResult.collectAsStateWithLifecycle()
            NotesScreen(
                notesResult = notesResult,
                snackbarHostState = remember { SnackbarHostState() },
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Notes")
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_empty_notes))
            .assertExists()
    }

    @Test
    fun loaded() {
        val notes = LessonNotesPreviewParameterProvider().values.first()

        rule.setContent {
            NotesScreen(
                notesResult = Result.Success(notes),
                snackbarHostState = remember { SnackbarHostState() },
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }

        rule.onRoot().printToLog("Lesson Notes")
        rule.onNodeWithText(rule.activity.getString(R.string.lesson_empty_notes))
            .assertDoesNotExist()
    }
}