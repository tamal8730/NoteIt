package com.github.tamal8730.noteit.feature_arrange_notes.repository.impl

import com.github.tamal8730.noteit.core.data.db.FakeDB
import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.feature_arrange_notes.repository.NotesArrangeRepository

class NotesArrangeRepositoryImpl : NotesArrangeRepository {
    override suspend fun getAllNotes(): List<NoteModel> {
        return FakeDB.getAllNotes()
    }
}