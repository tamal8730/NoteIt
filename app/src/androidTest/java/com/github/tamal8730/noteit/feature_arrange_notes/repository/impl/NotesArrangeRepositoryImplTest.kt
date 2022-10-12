package com.github.tamal8730.noteit.feature_arrange_notes.repository.impl

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tamal8730.noteit.core.data.dao.NoteDao
import com.github.tamal8730.noteit.core.data.db.NoteDb
import com.github.tamal8730.noteit.core.data.entity.NoteEntity
import com.github.tamal8730.noteit.core.data.entity.TaskEntity
import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import com.github.tamal8730.noteit.feature_arrange_notes.repository.NotesArrangeRepository
import com.github.tamal8730.noteit.feature_arrange_notes.view_model.NotesGridScreenUIState
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository
import com.github.tamal8730.noteit.feature_edit_note.repository.impl.NoteEditRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NotesArrangeRepositoryImplTest {

    private lateinit var db: NoteDb
    private lateinit var notesArrangeRepository: NotesArrangeRepository
    private lateinit var noteDao: NoteDao
    private val notes = listOf(
        NoteModel(
            7,
            "title1",
            "body1",
            "cover1",
            "2022-10-12T10:07:43",
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
            "2021-10-12T10:07:43",
            listOf(),
            78
        ),
        NoteModel(
            99,
            "title3",
            "",
            null,
            "2022-08-15T10:07:43",
            null,
            97
        )
    )

    private suspend fun saveNote(note: NoteModel, noteDao: NoteDao): Long {

        val rowID = noteDao.saveNote(
            NoteEntity(
                id = note.id,
                title = note.title ?: "",
                body = note.body,
                coverImage = note.coverImage,
                lastModifiedAt = note.lastModifiedAt,
                color = note.color
            )
        )

        note.tasks?.let {
            noteDao.saveTasks(
                it.map { task ->
                    TaskEntity(noteId = rowID, task = task.task, complete = task.complete)
                }
            )
        }

        return rowID

    }

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NoteDb::class.java).build()
        noteDao = db.noteDao()
        notesArrangeRepository = NotesArrangeRepositoryImpl(noteDao)
    }

    @Before
    @Throws(IOException::class)
    fun close() {
        db.close()
    }

    @Test
    fun getAllNotes_checkIfAllNotesLoaded() = runBlocking {


        notes.forEach { note ->
            saveNote(note, noteDao)
        }


        val notesQueried = notesArrangeRepository.getAllNotes().sortedBy { it.id }
        val expectedNotes = notes.sortedBy { it.id }

        assertEquals("sizes of notes list must be same", expectedNotes.size, notesQueried.size)

        for (i in expectedNotes.indices) {

            val expected = expectedNotes[i]
            val actual = notesQueried[i]

            assertEquals("id must be same", expected.id, actual.id)
            assertEquals("title must be same", expected.title, actual.title)
            assertEquals("body must be same", expected.body, actual.body)
            assertEquals("color must be same", expected.color, actual.color)
            assertEquals(
                "timestamp must be same",
                expected.lastModifiedAt,
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