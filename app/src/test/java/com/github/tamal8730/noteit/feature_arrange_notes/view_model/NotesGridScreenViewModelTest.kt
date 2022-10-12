package com.github.tamal8730.noteit.feature_arrange_notes.view_model

import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import com.github.tamal8730.noteit.feature_arrange_notes.repository.NotesArrangeRepository
import com.github.tamal8730.noteit.feature_arrange_notes.repository.impl.FakeNotesArrangeRepository
import com.github.tamal8730.noteit.feature_arrange_notes.util.last_edit_datetime_formatter.TimeOrDateDateTimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class NotesGridScreenViewModelTest {

    private lateinit var viewModel: NotesGridScreenViewModel
    private lateinit var repository: NotesArrangeRepository
    private val timestamps = listOf(
        "2022-10-12T10:07:43",
        "2021-10-12T10:07:43",
        "2022-08-15T10:07:43"
    )

    private val timestampToFormattedTimestampMap = mapOf(
        timestamps[0] to "10:07 am",
        timestamps[1] to "oct 12",
        timestamps[2] to "aug 15"
    )

    private val notes = listOf(
        NoteModel(
            7,
            "title1",
            "body1",
            "cover1",
            timestamps[0],
            listOf(
                TaskListItemModel("item1", false),
                TaskListItemModel("item2", true)
            ),
            9
        ),
        NoteModel(
            12,
            "",
            "body2",
            "cover2",
            timestamps[1],
            listOf(),
            78
        ),
        NoteModel(
            99,
            "title3",
            "",
            null,
            timestamps[2],
            null,
            97
        )
    )


    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeNotesArrangeRepository(notes)
        viewModel = NotesGridScreenViewModel(repository, TimeOrDateDateTimeFormatter())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `check if notes are loaded`() {

        assertTrue(
            "notes should have been loaded",
            viewModel.uiState.value is NotesGridScreenUIState.Loaded
        )

        val notesFound =
            (viewModel.uiState.value as NotesGridScreenUIState.Loaded).notes.sortedBy { it.id }

        val notesExpected = notes.sortedBy { it.id }

        assertEquals("sizes of notes list must be same", notesFound.size, notesExpected.size)

        for (i in notesFound.indices) {

            val expected = notesExpected[i]
            val actual = notesFound[i]

            assertEquals("id must be same", expected.id, actual.id)
            assertEquals("title must be same", expected.title, actual.title)
            assertEquals("body must be same", expected.body, actual.body)
            assertEquals("color must be same", expected.color, actual.color)
            assertEquals(
                "timestamp must be same",
                timestampToFormattedTimestampMap[expected.lastModifiedAt],
                actual.lastModifiedAt
            )
            assertEquals("cover image must be same", expected.coverImage, actual.coverImage)
            assertEquals(
                "tasks sizes must be same",
                expected.tasks?.size ?: 0,
                actual.tasks?.size ?: 0
            )

            val tasksExpected = (expected.tasks ?: listOf()).sortedBy { it.task }
            val tasksActual = (actual.tasks ?: listOf()).sortedBy { it.task }

            var taskIdx = 0
            assertTrue("tasks must be same", tasksExpected.stream().allMatch {
                it.task == tasksActual[taskIdx].task
                        && it.complete == tasksActual[taskIdx++].complete
            })

        }


    }


}