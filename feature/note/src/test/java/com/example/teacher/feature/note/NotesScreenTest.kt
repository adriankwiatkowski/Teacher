package com.example.teacher.feature.note

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
import com.example.teacher.core.data.repository.note.NoteRepository
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.NotesPreviewParameterProvider
import com.example.teacher.feature.note.data.NotesViewModel
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
internal class NotesScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var noteRepository: NoteRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun notesLoaded() {
        val viewModel = ViewModelProvider(rule.activity)[NotesViewModel::class.java]
        val notesResult = viewModel.notesResult

        rule.setContent {
            val notesResultState by notesResult.collectAsStateWithLifecycle()
            NotesScreen(
                notesResult = notesResultState,
                snackbarHostState = remember { SnackbarHostState() },
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }

        rule.onRoot().printToLog("Notes")
        rule.waitUntil { notesResult.value is Result.Success }
    }

    @Test
    fun notesDisplayed() = runTest {
        val viewModel = ViewModelProvider(rule.activity)[NotesViewModel::class.java]
        givenNotes(limit = 1)
        val notesResult = viewModel.notesResult

        rule.setContent {
            val notesResultState by notesResult.collectAsStateWithLifecycle()
            NotesScreen(
                notesResult = notesResultState,
                snackbarHostState = remember { SnackbarHostState() },
                onNoteClick = {},
                onAddNoteClick = {},
            )
        }

        rule.onRoot().printToLog("Notes")
        rule.waitUntil { notesResult.value is Result.Success }

        rule.onRoot().printToLog("Notes Fetched")
        rule.onNodeWithText(notes.first().title).assertExists()
        rule.onNodeWithText(notes.first().text).assertExists()
    }

    private suspend fun givenNotes(limit: Int = Int.MAX_VALUE) {
        for (note in notes.take(limit)) {
            noteRepository.upsertNote(
                id = null,
                title = note.title,
                text = note.text,
                priority = note.priority,
            )
        }
    }

    private val notes = NotesPreviewParameterProvider().values.first()
}