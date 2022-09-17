package com.github.tamal8730.noteit.feature_edit_note.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.TaskUIModel
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteEditScreenViewModelFactory(
    private val noteEditRepository: NoteEditRepository,
    private val noteID: String,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        NoteEditScreenViewModel(noteEditRepository, noteID) as T

}

class NoteEditScreenViewModel(
    private val noteEditRepository: NoteEditRepository,
    private val noteID: String,
) : ViewModel() {

    private val _title by lazy { MutableStateFlow<String>("") }
    val title: StateFlow<String> by lazy { _title.asStateFlow() }

    private val _body by lazy { MutableStateFlow<String>("") }
    val body: StateFlow<String> by lazy { _body.asStateFlow() }

    private val _coverImageAdded by lazy { MutableStateFlow<Boolean>(false) }
    val coverImageAdded: StateFlow<Boolean> by lazy { _coverImageAdded.asStateFlow() }

    private val _coverImageUri by lazy { MutableStateFlow<Uri?>(null) }
    val coverImageUri: StateFlow<Uri?> by lazy { _coverImageUri.asStateFlow() }

    private val _tasksAdded by lazy { MutableStateFlow<Boolean>(false) }
    val tasksAdded: StateFlow<Boolean> by lazy { _tasksAdded.asStateFlow() }

    private val _tasks by lazy { MutableStateFlow<List<TaskUIModel>>(listOf()) }
    val tasks: StateFlow<List<TaskUIModel>> by lazy { _tasks.asStateFlow() }


    init {
        loadNote()
    }


    fun editTitle(title: String) {
        _title.value = title
    }

    fun editBody(body: String) {
        _body.value = body
    }

    //-------------- Task list--------------
    fun addTaskList() {
        _tasksAdded.value = true
        _tasks.value = listOf()
    }

    fun removeTaskList() {
        _tasksAdded.value = false
        _tasks.value = listOf()
    }

    fun addTask() {
        if (_tasksAdded.value) {
            _tasks.value += TaskUIModel("", false)
        }
    }

    fun removeTask(task: TaskUIModel) {
        _tasks.value = _tasks.value.filter { it != task }
    }

    fun editTask(task: TaskUIModel, text: String) {
        _tasks.value = _tasks.value.map { if (it == task) it.copy(task = text) else it }
    }

    fun checkTask(task: TaskUIModel) {
        _tasks.value = _tasks.value.map { if (it == task) it.copy(complete = true) else it }
    }

    fun unCheckTask(task: TaskUIModel) {
        _tasks.value = _tasks.value.map { if (it == task) it.copy(complete = false) else it }
    }

    //-----------------------------------


    //-------------- Cover image--------------
    fun addCoverImage(uri: Uri) {
        _coverImageAdded.value = true
        _coverImageUri.value = uri
    }

    fun removeCoverImage() {
        _coverImageAdded.value = false
        _coverImageUri.value = null
    }
    //-----------------------------------


    private fun loadNote() = viewModelScope.launch {
        val note = noteEditRepository.loadNote(noteID) ?: return@launch
        _title.value = note.title ?: ""
        _body.value = note.body
        _coverImageAdded.value = note.coverImage != null
        _coverImageUri.value = if (note.coverImage != null) Uri.parse(note.coverImage) else null
        _tasksAdded.value = note.tasks != null
        _tasks.value = note.tasks?.map {
            TaskUIModel(it.task, it.complete)
        } ?: listOf()
    }

}