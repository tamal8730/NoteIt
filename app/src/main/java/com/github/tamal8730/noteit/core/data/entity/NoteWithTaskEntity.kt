package com.github.tamal8730.noteit.core.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithTasksEntity(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    ) val tasks: List<TaskEntity>
)
