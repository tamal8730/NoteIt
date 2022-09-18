package com.github.tamal8730.noteit.core.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.tamal8730.noteit.core.data.db.NoteDb
import com.github.tamal8730.noteit.feature_arrange_notes.repository.impl.NotesArrangeRepositoryImpl
import com.github.tamal8730.noteit.feature_arrange_notes.util.last_edit_datetime_formatter.TimeOrDateDateTimeFormatter
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.NotesGridScreen
import com.github.tamal8730.noteit.feature_arrange_notes.view_model.NotesGridScreenViewModelFactory
import com.github.tamal8730.noteit.feature_edit_note.repository.impl.NoteEditRepositoryImpl
import com.github.tamal8730.noteit.feature_edit_note.util.LastUpdatedDateTimeFormatter
import com.github.tamal8730.noteit.feature_edit_note.view.note_edit_screen.NoteEditScreen
import com.github.tamal8730.noteit.feature_edit_note.view_model.NoteEditScreenViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun SetupNavGraph(navController: NavHostController) {

    val context = LocalContext.current
    val noteDao = NoteDb.getInstance(context).noteDao()
    val systemUiController = rememberSystemUiController()

    NavHost(navController = navController, startDestination = Screen.NotesGridScreen.route) {

        composable(route = Screen.NotesGridScreen.route) {

            NotesGridScreen(
                systemUiController = systemUiController,
                viewModel = viewModel(
                    factory = NotesGridScreenViewModelFactory(
                        notesArrangeRepository = NotesArrangeRepositoryImpl(noteDao = noteDao),
                        lastEditTimeFormatter = TimeOrDateDateTimeFormatter()
                    )
                ),
                onEditNote = {
                    navController.navigate(Screen.EditNoteScreen.getFullPath(it.toString())) {
                        launchSingleTop = true
                    }
                },
                onCreateNewNote = {
                    navController.navigate(Screen.EditNoteScreen.getFullPath(null)) {
                        launchSingleTop = true
                    }
                }
            )

        }

        composable(
            route = Screen.EditNoteScreen.route,
            arguments = listOf(
                navArgument(Screen.EditNoteScreen.argNoteID) {
                    type = NavType.StringType; nullable = true; defaultValue = null
                },
            )
        ) { backStackEntry ->

            val noteID = backStackEntry.arguments?.getString(Screen.EditNoteScreen.argNoteID)

            NoteEditScreen(
                systemUiController = systemUiController,
                viewModel = viewModel(
                    factory = NoteEditScreenViewModelFactory(
                        noteEditRepository = NoteEditRepositoryImpl(noteDao),
                        noteID = noteID?.toLong(),
                        lastUpdateTimestampFormatter = LastUpdatedDateTimeFormatter()
                    )
                ),
                onBack = {
                    navController.popBackStack()
                },
                onDelete = {
                    navController.popBackStack()
                }
            )


        }

    }
}