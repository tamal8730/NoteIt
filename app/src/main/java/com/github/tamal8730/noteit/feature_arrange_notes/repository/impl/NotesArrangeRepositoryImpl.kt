package com.github.tamal8730.noteit.feature_arrange_notes.repository.impl

import com.github.tamal8730.noteit.core.data.dao.NoteDao
import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import com.github.tamal8730.noteit.feature_arrange_notes.repository.NotesArrangeRepository

class NotesArrangeRepositoryImpl(private val noteDao: NoteDao) :
    NotesArrangeRepository {

    override suspend fun getAllNotes(): List<NoteModel> {

        return noteDao.getNotes().map {
            val note = it.note
            val tasks = it.tasks
            NoteModel(
                id = note.id,
                title = note.title,
                body = note.body,
                coverImage = note.coverImage,
                lastModifiedAt = note.lastModifiedAt,
                tasks = tasks.map { t ->
                    TaskListItemModel(t.task, t.complete)
                },
                color = note.color,
            )
        }
    }


}