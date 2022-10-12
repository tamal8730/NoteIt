package com.github.tamal8730.noteit.feature_edit_note.repository.impl

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tamal8730.noteit.core.data.dao.NoteDao
import com.github.tamal8730.noteit.core.data.db.NoteDb
import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NoteEditRepositoryImplTest {

    private lateinit var db: NoteDb
    private lateinit var noteEditRepository: NoteEditRepository
    private lateinit var noteDao: NoteDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NoteDb::class.java).build()
        noteDao = db.noteDao()
        noteEditRepository = NoteEditRepositoryImpl(noteDao)
    }

    @Before
    @Throws(IOException::class)
    fun close() {
        db.close()
    }

    private fun noteModelsSame(note1: NoteModel, note2: NoteModel): Boolean {
        return note1.id == note2.id &&
                note1.title == note2.title
                && note1.body == note2.body
                && note1.coverImage == note2.coverImage
                && note1.color == note2.color
                && note1.lastModifiedAt == note2.lastModifiedAt
                && (note1.tasks ?: listOf()).size == (note2.tasks ?: listOf()).size
                && (note1.tasks ?: listOf()).stream()
            .allMatch { (note2.tasks ?: listOf()).contains(it) }
    }

    @Test
    fun saveNote_isNoteSaved() {

        val note1 = NoteModel(
            777,
            "title1",
            "body2",
            "cover1",
            "1",
            listOf(
                TaskListItemModel("item1", false),
                TaskListItemModel("item2", true)
            ),
            9
        )

        val note2 = NoteModel(
            678,
            "title2",
            "body2",
            "cover2",
            "2",
            listOf(),
            4
        )

        runBlocking {

            noteEditRepository.saveNote(note1)
            noteEditRepository.saveNote(note2)

            var note = noteEditRepository.loadNote(777)
            assert(
                note != null && noteModelsSame(
                    note1,
                    note
                )
            ) { "saved note and queried note must be same" }

            note = noteEditRepository.loadNote(678)
            assert(
                note != null && noteModelsSame(
                    note2,
                    note
                )
            ) { "saved note and queried note must be same" }
        }
    }

    @Test
    fun deleteNote_isNoteDeleted() {

        val note1 = NoteModel(
            777,
            "title1",
            "body2",
            "cover1",
            "1",
            listOf(
                TaskListItemModel("item1", false),
                TaskListItemModel("item2", true)
            ),
            9
        )

        val note2 = NoteModel(
            678,
            "title2",
            "body2",
            "cover2",
            "2",
            listOf(),
            4
        )

        runBlocking {

            noteEditRepository.saveNote(note1)
            noteEditRepository.saveNote(note2)

            var note = noteEditRepository.loadNote(678)
            assert(
                note != null && noteModelsSame(
                    note2,
                    note
                )
            ) { "saved note and queried note must be same" }

            noteEditRepository.deleteNote(678)

            note = noteEditRepository.loadNote(678)
            assertNull("note not removed", note)

        }

    }

}