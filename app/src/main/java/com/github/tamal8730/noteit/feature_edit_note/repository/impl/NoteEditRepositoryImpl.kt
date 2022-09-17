package com.github.tamal8730.noteit.feature_edit_note.repository.impl

import com.github.tamal8730.noteit.core.data.db.FakeDB
import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository
import kotlinx.coroutines.delay

class NoteEditRepositoryImpl : NoteEditRepository {

    override suspend fun saveNote(note: NoteModel) {
        FakeDB.addNote(note)
    }

    override suspend fun loadNote(id: String): NoteModel? {
        return FakeDB.getNote(id)
    }

}