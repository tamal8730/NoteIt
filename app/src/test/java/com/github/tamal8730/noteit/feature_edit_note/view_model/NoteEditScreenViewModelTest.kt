package com.github.tamal8730.noteit.feature_edit_note.view_model

import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository
import com.github.tamal8730.noteit.feature_edit_note.repository.impl.FakeNoteEditRepository
import com.github.tamal8730.noteit.feature_edit_note.util.LastUpdatedDateTimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch


@OptIn(ExperimentalCoroutinesApi::class)
class NoteEditScreenViewModelTest {

    private lateinit var noteEditScreenViewModel: NoteEditScreenViewModel
    private lateinit var repository: NoteEditRepository
    private var noteId: Long = 0

    private val note = NoteModel(
        777,
        "title1",
        "body2",
        "cover1",
        "2022-10-12T10:07:43",
        listOf(
            TaskListItemModel("item1", false),
            TaskListItemModel("item2", true)
        ),
        9
    )

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {

        Dispatchers.setMain(testDispatcher)

        repository = FakeNoteEditRepository()

        runTest {

            noteId = repository.saveNote(note)

            noteEditScreenViewModel = NoteEditScreenViewModel(
                repository,
                noteId,
                LastUpdatedDateTimeFormatter(),
                autosave = false
            )

        }

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check if note is loaded`() {
        assertEquals("title must be same", note.title, noteEditScreenViewModel.title.value)
        assertEquals("body must be same", note.body, noteEditScreenViewModel.body.value)
        assertEquals(
            "cover image path must be same",
            note.coverImage,
            noteEditScreenViewModel.coverImageUri.value
        )
        assertEquals("color must be same", note.color, noteEditScreenViewModel.noteColor.value)

        val expectedTasks = note.tasks ?: listOf()
        val retrievedTasks = noteEditScreenViewModel.tasks.value

        assertTrue("tasks must be of same size", expectedTasks.size == retrievedTasks.size)

        var idx = 0
        assertTrue("tasks list must be same", expectedTasks.stream().allMatch {
            it.task == retrievedTasks[idx].task && it.complete == retrievedTasks[idx++].complete
        })

    }

    @Test
    fun editTitle_isTitleChanged() {
        val newTitle = "edited title"
        noteEditScreenViewModel.editTitle(newTitle)
        assertEquals("title not changed", noteEditScreenViewModel.title.value, newTitle)
    }

    @Test
    fun deleteNote_isNoteDeleted() {
        runTest {
            noteEditScreenViewModel.deleteNote {}
            assertNull("note not deleted", repository.loadNote(noteId))
        }
    }


}