package com.github.tamal8730.noteit.feature_edit_note.repository.impl

import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository

class FakeNoteEditRepository : NoteEditRepository {

    private val store = mutableMapOf<Long, NoteModel>()

    override suspend fun saveNote(note: NoteModel): Long {
        val id = store.size.toLong()
        store[id] = note
        return id
    }

    override suspend fun loadNote(id: Long): NoteModel? {
        return store[id]
    }

    override suspend fun deleteNote(id: Long) {
        store.remove(id)
    }


}