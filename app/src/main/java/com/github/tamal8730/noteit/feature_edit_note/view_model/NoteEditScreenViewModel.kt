package com.github.tamal8730.noteit.feature_edit_note.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import com.github.tamal8730.noteit.core.util.DateTime
import com.github.tamal8730.noteit.core.util.TimestampFormatter
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.TaskUIModel
import com.github.tamal8730.noteit.feature_edit_note.repository.NoteEditRepository
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteEditScreenViewModelFactory(
    private val noteEditRepository: NoteEditRepository,
    private val noteID: Long?,
    private val lastUpdateTimestampFormatter: TimestampFormatter,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        NoteEditScreenViewModel(noteEditRepository, noteID, lastUpdateTimestampFormatter) as T

}

class NoteEditScreenViewModel(
    private val noteEditRepository: NoteEditRepository,
    private var noteID: Long?,
    private val lastUpdateTimestampFormatter: TimestampFormatter,
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

    private val _lastUpdatedTimestamp by lazy { MutableStateFlow<String>("") }
    val lastUpdatedTimestamp: StateFlow<String> = _lastUpdatedTimestamp.asStateFlow()

    private val _noteColor by lazy { MutableStateFlow<Long?>(null) }
    val noteColor: StateFlow<Long?> = _noteColor.asStateFlow()

    private val _colorMenuShown by lazy { MutableStateFlow<Boolean>(false) }
    val colorMenuShown: StateFlow<Boolean> = _colorMenuShown.asStateFlow()


    init {
        loadNote()
        saveNote()
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

    private fun isNoteEmpty(): Boolean {
        return _title.value.isBlank()
                && _body.value.isBlank()
                && !(coverImageAdded.value)
                && coverImageUri.value == null
                && !(tasksAdded.value)
                && tasks.value.isEmpty()
    }

    private fun saveNote() = viewModelScope.launch {

        delay(5000)

        while (true) {

            if (!isNoteEmpty()) {

                _lastUpdatedTimestamp.value = "saving..."
                val updatedTime = DateTime.now().toISO8601Timestamp()

                val note = NoteModel(
                    id = noteID ?: 0,
                    title = _title.value,
                    body = _body.value,
                    coverImage = _coverImageUri.value?.toString(),
                    lastModifiedAt = updatedTime,
                    tasks = if (_tasks.value.isEmpty()) null
                    else _tasks.value.map {
                        TaskListItemModel(it.task, it.complete)
                    },
                    color = _noteColor.value,
                )

                noteID = noteEditRepository.saveNote(note)
                _lastUpdatedTimestamp.value = "Updated " +
                        lastUpdateTimestampFormatter.format(updatedTime)
            }

            delay(3000)


        }

    }

    private fun loadNote() = viewModelScope.launch {

        val noteID = this@NoteEditScreenViewModel.noteID

        val note = if (noteID == null) return@launch
        else noteEditRepository.loadNote(noteID) ?: return@launch

        _title.value = note.title ?: ""
        _body.value = note.body
        _coverImageAdded.value = note.coverImage != null
        _coverImageUri.value = if (note.coverImage != null) Uri.parse(note.coverImage) else null
        _tasksAdded.value = note.tasks != null
        _tasks.value = note.tasks?.map {
            TaskUIModel(it.task, it.complete)
        } ?: listOf()
        _lastUpdatedTimestamp.value = "Updated " +
                lastUpdateTimestampFormatter.format(note.lastModifiedAt)
        _noteColor.value = note.color
    }


    fun toggleColorMenu() {
        _colorMenuShown.value = !_colorMenuShown.value
    }

    fun dismissColorMenu() {
        _colorMenuShown.value = false
    }

    fun setNoteColor(color: Long) {
        _noteColor.value = color
        _colorMenuShown.value = false
    }

}