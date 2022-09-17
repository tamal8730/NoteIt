package com.github.tamal8730.noteit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.tamal8730.noteit.core.navigation.SetupNavGraph
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.NotesGridScreen
import com.github.tamal8730.noteit.feature_edit_note.view.note_edit_screen.NoteEditScreen
import com.github.tamal8730.noteit.ui.theme.NoteItTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteItTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}