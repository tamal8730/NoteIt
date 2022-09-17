package com.github.tamal8730.noteit.core.model

data class NoteModel(
    val title: String? = null,
    val body: String = "",
    val coverImage: String? = null,
    val lastModifiedAt: String,
    val tasks: List<TaskListItemModel>? = null,
    val color: Int? = null,
)
