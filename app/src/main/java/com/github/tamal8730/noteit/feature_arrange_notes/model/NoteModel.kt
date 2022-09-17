package com.github.tamal8730.noteit.feature_arrange_notes.model

data class NoteModel(
    val title: String?,
    val body: String,
    val coverImage: String?,
    val lastModifiedAt: String,
    val tasks: List<TaskListItemModel>?,
    val color: Int?,
)
