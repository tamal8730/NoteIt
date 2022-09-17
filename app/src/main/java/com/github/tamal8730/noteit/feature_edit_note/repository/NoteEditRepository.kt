package com.github.tamal8730.noteit.feature_edit_note.repository

import com.github.tamal8730.noteit.core.model.NoteModel

interface NoteEditRepository {

    suspend fun saveNote(note: NoteModel)
    suspend fun loadNote(id: String): NoteModel?

}