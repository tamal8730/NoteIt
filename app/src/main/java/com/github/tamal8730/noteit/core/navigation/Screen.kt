package com.github.tamal8730.noteit.core.navigation

sealed class Screen(val route: String) {
    object EditNoteScreen : Screen("edit_note_screen")
}
