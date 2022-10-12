package com.github.tamal8730.noteit.feature_arrange_notes.repository.impl

import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.feature_arrange_notes.repository.NotesArrangeRepository

class FakeNotesArrangeRepository(private val notes: List<NoteModel>) : NotesArrangeRepository {

    override suspend fun getAllNotes(): List<NoteModel> {
        return notes
    }


}