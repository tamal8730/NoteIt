package com.github.tamal8730.noteit.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.tamal8730.noteit.feature_arrange_notes.repository.impl.NotesArrangeRepositoryImpl
import com.github.tamal8730.noteit.feature_arrange_notes.util.last_edit_datetime_formatter.TimeOrDateDateTimeFormatter
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.NotesGridScreen
import com.github.tamal8730.noteit.feature_arrange_notes.view_model.NotesGridScreenViewModelFactory
import com.github.tamal8730.noteit.feature_edit_note.repository.impl.NoteEditRepositoryImpl
import com.github.tamal8730.noteit.feature_edit_note.view.note_edit_screen.NoteEditScreen
import com.github.tamal8730.noteit.feature_edit_note.view_model.NoteEditScreenViewModelFactory


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.NotesGridScreen.route) {

        composable(route = Screen.NotesGridScreen.route) {

            NotesGridScreen(
                viewModel = viewModel(
                    factory = NotesGridScreenViewModelFactory(
                        notesArrangeRepository = NotesArrangeRepositoryImpl(),
                        lastEditTimeFormatter = TimeOrDateDateTimeFormatter()
                    )
                ),
                onEditNote = {
                    navController.navigate(Screen.EditNoteScreen.getFullPath(it)) {
                        launchSingleTop = true
                    }
                },
                onCreateNewNote = {
                    navController.navigate(Screen.EditNoteScreen.route) {
                        launchSingleTop = true
                    }
                }
            )

        }

        composable(
            route = Screen.EditNoteScreen.route,
            arguments = listOf(
                navArgument(Screen.EditNoteScreen.argNoteID) { type = NavType.StringType },
            )
        ) { backStackEntry ->

            val noteID = backStackEntry.arguments?.getString(Screen.EditNoteScreen.argNoteID)

            NoteEditScreen(
                viewModel = viewModel(
                    factory = NoteEditScreenViewModelFactory(
                        noteEditRepository = NoteEditRepositoryImpl(),
                        noteID = noteID
                    )
                ),
                onBack = {
                    navController.popBackStack()
                }
            )


        }

    }
}