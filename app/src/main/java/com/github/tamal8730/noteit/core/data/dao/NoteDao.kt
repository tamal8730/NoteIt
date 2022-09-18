package com.github.tamal8730.noteit.core.data.dao

import androidx.room.*
import com.github.tamal8730.noteit.core.data.entity.NoteEntity
import com.github.tamal8730.noteit.core.data.entity.NoteWithTasksEntity
import com.github.tamal8730.noteit.core.data.entity.TaskEntity

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM notes WHERE id=:id")
    suspend fun getNote(id: Long): NoteWithTasksEntity?

    @Transaction
    @Query("SELECT * FROM notes ORDER BY lastModifiedAt DESC")
    suspend fun getNotes(): List<NoteWithTasksEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTasks(tasks: List<TaskEntity>)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

}