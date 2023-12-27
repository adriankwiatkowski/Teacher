package com.example.teacher.core.database.datasource.note

import com.example.teacher.core.database.createTeacherDatabase
import com.example.teacher.core.model.data.Note
import com.example.teacher.core.model.data.NotePriority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class NoteDataSourceTest {

    private lateinit var noteDataSource: NoteDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val db = createTeacherDatabase()
        noteDataSource = NoteDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun getNotes() = runTest {
        givenNote()

        val actual = noteDataSource.getNotes().first()

        assertEquals(1, actual.size)
    }

    @Test
    fun getNoteById() = runTest {
        givenNote()

        val actual = noteDataSource.getNoteById(1L).first()

        assertNotNull(actual)
        assertEquals(1L, actual?.id)
    }

    @Test
    fun insertNote() = runTest {
        val actual =
            givenNote(title = "Insert Title", text = "Insert Text", priority = NotePriority.High)

        assertNotNull(actual)
        assertEquals(1L, actual.id)
        assertEquals("Insert Title", actual.title)
        assertEquals("Insert Text", actual.text)
        assertEquals(NotePriority.High, actual.priority)
    }

    @Test
    fun updateNote() = runTest {
        givenNote()
        val actual = givenNote(
            id = 1L,
            title = "Update Title",
            text = "Update Text",
            priority = NotePriority.Low,
        )

        assertNotNull(actual)
        assertEquals(1L, actual.id)
        assertEquals("Update Title", actual.title)
        assertEquals("Update Text", actual.text)
        assertEquals(NotePriority.Low, actual.priority)
    }

    @Test
    fun deleteNoteById() = runTest {
        givenNote()

        noteDataSource.deleteNoteById(1L)
        val notes = noteDataSource.getNotes().first()

        assertEquals(0, notes.size)
    }

    private suspend fun givenNote(
        id: Long? = null,
        title: String = "Title",
        text: String = "Text",
        priority: NotePriority = NotePriority.Medium,
    ): Note {
        noteDataSource.insertOrUpdateNote(
            id = id,
            title = title,
            text = text,
            priority = priority,
        )

        return if (id == null) {
            noteDataSource.getNotes().first().last()
        } else {
            noteDataSource.getNoteById(id).first()!!
        }
    }
}