package com.github.tamal8730.noteit.core.data.db

import com.github.tamal8730.noteit.core.model.NoteModel
import kotlinx.coroutines.delay

object FakeDB {

    private val notes = mutableMapOf<String, NoteModel>()

    suspend fun addNote(note: NoteModel) {
        delay(1000)
        val key = (notes.size + 1).toString()
        notes[key] = note
    }

    suspend fun getNote(id: String): NoteModel? {
        delay(1000)
        return notes[id]
    }

    suspend fun getAllNotes(): List<NoteModel> {
        delay(1000)
        return notes.map { it.value }
    }

}