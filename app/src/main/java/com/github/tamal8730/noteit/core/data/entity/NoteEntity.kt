package com.github.tamal8730.noteit.core.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.tamal8730.noteit.core.model.TaskListItemModel

@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val body: String,

    val coverImage: String?,
    val lastModifiedAt: String,

    val color: Long?

)