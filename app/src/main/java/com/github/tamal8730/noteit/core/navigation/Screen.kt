package com.github.tamal8730.noteit.core.navigation

sealed class Screen(val route: String) {
    object NotesGridScreen : Screen("notes_grid_screen")
    object EditNoteScreen : Screen("edit_note_screen")
}
