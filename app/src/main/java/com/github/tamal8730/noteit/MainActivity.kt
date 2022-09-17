package com.github.tamal8730.noteit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.NotesGridScreen
import com.github.tamal8730.noteit.feature_edit_note.view.note_edit_screen.NoteEditScreen
import com.github.tamal8730.noteit.ui.theme.NoteItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteItTheme {
                NoteEditScreen()
            }
        }
    }
}