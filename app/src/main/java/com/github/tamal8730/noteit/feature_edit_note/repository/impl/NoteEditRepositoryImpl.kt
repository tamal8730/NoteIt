package com.github.tamal8730.noteit.feature_edit_note.repository.impl

import com.github.tamal8730.noteit.core.data.dao.NoteDao
import com.github.tamal8730.noteit.core.data.entity.NoteEntity
import com.github.tamal8730.noteit.core.data.entity.TaskEntity
import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository

class NoteEditRepositoryImpl(private val noteDao: NoteDao) : NoteEditRepository {

    override suspend fun saveNote(note: NoteModel): Long {

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

    override suspend fun loadNote(id: Long): NoteModel? {
        val entity = noteDao.getNote(id) ?: return null
        return NoteModel(
            id = id,
            title = entity.note.title,
            body = entity.note.body,
            coverImage = entity.note.coverImage,
            lastModifiedAt = entity.note.lastModifiedAt,
            color = entity.note.color,
            tasks = if (entity.tasks.isEmpty()) null
            else entity.tasks.map {
                TaskListItemModel(it.task, it.complete)
            }
        )
    }

}