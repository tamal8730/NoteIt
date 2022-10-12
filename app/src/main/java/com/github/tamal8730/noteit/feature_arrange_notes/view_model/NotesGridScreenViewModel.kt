package com.github.tamal8730.noteit.feature_arrange_notes.view_model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.tamal8730.noteit.util.TimestampFormatter
import com.github.tamal8730.noteit.feature_arrange_notes.repository.NotesArrangeRepository
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.NoteUIModel
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.TaskUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesGridScreenViewModelFactory(
    private val notesArrangeRepository: NotesArrangeRepository,
    private val lastEditTimeFormatter: TimestampFormatter
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        NotesGridScreenViewModel(notesArrangeRepository, lastEditTimeFormatter) as T

}

sealed class NotesGridScreenUIState {
    object Empty : NotesGridScreenUIState()
    object Loading : NotesGridScreenUIState()
    object NoNotes : NotesGridScreenUIState()
    data class Loaded(val notes: List<NoteUIModel>) : NotesGridScreenUIState()
}

class NotesGridScreenViewModel(
    private val notesArrangeRepository: NotesArrangeRepository,
    private val lastEditTimeFormatter: TimestampFormatter,
    private val loadNotesOnInit: Boolean = true,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesGridScreenUIState>(NotesGridScreenUIState.Empty)
    val uiState: StateFlow<NotesGridScreenUIState> = _uiState.asStateFlow()


    init {
        if (loadNotesOnInit)
            loadAllNotes()
    }

    fun loadAllNotes() = viewModelScope.launch {
        if (_uiState.value !is NotesGridScreenUIState.Loaded) {
            _uiState.value = NotesGridScreenUIState.Loading
        }
        val allNotes = notesArrangeRepository.getAllNotes()
        if (allNotes.isEmpty()) {
            _uiState.value = NotesGridScreenUIState.NoNotes
        } else {
            _uiState.value = NotesGridScreenUIState.Loaded(
                allNotes.map {

                    NoteUIModel(
                        id = it.id,
                        title = it.title ?: "",
                        body = it.body,
                        coverImage = it.coverImage,
                        lastModifiedAt = lastEditTimeFormatter.format(it.lastModifiedAt),
                        tasks = it.tasks?.map { task -> TaskUIModel(task.task, task.complete) },
                        color = it.color
                    )

                }
            )
        }
    }

}