package com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model

import android.net.Uri

data class NoteUIModel(
    val id: Long,
    val title: String,
    val body: String?,
    val coverImage: Uri?,
    val lastModifiedAt: String,
    val tasks: List<TaskUIModel>?,
    val color: Long?,
)
