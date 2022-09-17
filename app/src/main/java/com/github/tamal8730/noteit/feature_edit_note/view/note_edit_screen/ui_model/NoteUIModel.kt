package com.github.tamal8730.noteit.feature_edit_note.view.note_edit_screen.ui_model

import android.net.Uri
import com.github.tamal8730.noteit.core.model.TaskListItemModel

data class NoteUIModel(
    val title: String? = null,
    val body: String = "",
    val coverImage: Uri? = null,
    val lastModifiedAt: String,
    val tasks: List<TaskListItemModel>? = null,
    val color: Int? = null,
)
