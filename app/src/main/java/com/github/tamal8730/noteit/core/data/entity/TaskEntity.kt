package com.github.tamal8730.noteit.core.data.entity

import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("noteId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskID: Long = 0,
    val noteId: Long,
    val task: String,
    val complete: Boolean
)