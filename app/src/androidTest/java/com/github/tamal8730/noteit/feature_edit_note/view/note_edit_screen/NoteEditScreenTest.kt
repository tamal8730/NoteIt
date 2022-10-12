package com.github.tamal8730.noteit.feature_edit_note.view.note_edit_screen

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.tamal8730.noteit.MainActivity
import com.github.tamal8730.noteit.core.navigation.Screen
import com.github.tamal8730.noteit.feature_edit_note.repository.impl.NoteEditRepositoryImpl
import com.github.tamal8730.noteit.feature_edit_note.util.LastUpdatedDateTimeFormatter
import com.github.tamal8730.noteit.feature_edit_note.view_model.NoteEditScreenViewModelFactory
import com.github.tamal8730.noteit.ui.theme.NoteItTheme
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteEditScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {


        composeRule.setContent {

            val navController = rememberNavController()
            NoteItTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.EditNoteScreen.route
                ) {

                    composable(route = Screen.EditNoteScreen.route) {

//                        NoteEditScreen(
//                            viewModel = viewModel(
//                                factory = NoteEditScreenViewModelFactory(
//                                    noteEditRepository = NoteEditRepositoryImpl(noteDao),
//                                    noteID = noteID?.toLong(),
//                                    lastUpdateTimestampFormatter = LastUpdatedDateTimeFormatter()
//                                )
//                            ),
//                            onBack = {
//                                navController.popBackStack()
//                            },
//                            onDelete = {
//                                navController.popBackStack()
//                            }
//                        )

                    }

                }
            }


        }
    }

    @Test
    fun a() {


    }

}