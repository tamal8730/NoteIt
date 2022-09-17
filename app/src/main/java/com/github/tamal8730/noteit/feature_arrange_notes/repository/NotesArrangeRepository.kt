package com.github.tamal8730.noteit.feature_arrange_notes.repository

import com.github.tamal8730.noteit.core.model.NoteModel

interface NotesArrangeRepository {

    suspend fun getAllNotes(): List<NoteModel>

}