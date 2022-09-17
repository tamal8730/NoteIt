package com.github.tamal8730.noteit.feature_edit_note.view.note_edit_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tamal8730.noteit.core.view.NoteItTextField
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.TaskUIModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NoteEditScreen() {

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var showList by remember { mutableStateOf(false) }
    var showCoverImage by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf(listOf<TaskUIModel>()) }

    var coverImageURI by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                coverImageURI = it; showCoverImage = true
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Close, "close")
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = 0.dp
            )
        },
        bottomBar = {
            TextOptionsBar(
                onToggleList = { showList = it },
                onToggleCoverImage = {
                    if (it) {
                        launcher.launch("image/*")
                    } else {
                        coverImageURI = null
                        showCoverImage = false
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it))
        {
            Column {

                if (showCoverImage) {
                    CoverImage(coverImageURI)
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    NoteItTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        textStyle = MaterialTheme.typography.h5.copy(
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold
                        ),
                        hint = "Title",
                        hintStyle = MaterialTheme.typography.h5
                            .copy(
                                color = MaterialTheme.colors.onBackground.copy(
                                    alpha = ContentAlpha.medium
                                )
                            ),
                        onValueChanged = { text -> title = text }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NoteItTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = body,
                        textStyle = MaterialTheme.typography.body1.copy(
                            color = MaterialTheme.colors.onBackground,
                            lineHeight = 26.sp
                        ),
                        hint = "Write something...",
                        hintStyle = MaterialTheme.typography.body1
                            .copy(
                                color = MaterialTheme.colors.onBackground.copy(
                                    alpha = ContentAlpha.medium
                                )
                            ),
                        onValueChanged = { text -> body = text }
                    )
                    if (showList) {
                        Spacer(modifier = Modifier.height(24.dp))
                        TaskList(
                            tasks = tasks,
                            onAddNewItem = {
                                tasks = tasks + TaskUIModel("", false)
                            },
                            onEditTask = { task, text ->
                                tasks = tasks.map { taskModel ->
                                    if (taskModel == task) taskModel.copy(task = text) else taskModel
                                }
                            },
                            onToggleTask = { task, checked ->
                                tasks = tasks.map { taskModel ->
                                    if (taskModel == task) taskModel.copy(complete = !checked) else taskModel
                                }
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun CoverImage(coverImageURI: Uri?) {
    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f),
        imageModel = coverImageURI,
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop
        )
    )
}

@Composable
private fun TaskList(
    tasks: List<TaskUIModel>,
    onAddNewItem: () -> Unit,
    onEditTask: (TaskUIModel, String) -> Unit,
    onToggleTask: (TaskUIModel, Boolean) -> Unit
) {

    Column {

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            tasks.map {
                ListItem(
                    task = it.task,
                    selected = it.complete,
                    { text ->
                        onEditTask(it, text)
                    },
                    { checked ->
                        onToggleTask(it, checked)
                    }
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.clickable {
            onAddNewItem()
        }) {
            Icon(Icons.Filled.Add, contentDescription = "add task")
            Spacer(modifier = Modifier.width(16.dp))
            Text("Add new item")
        }
    }

}


@Composable
private fun ListItem(
    task: String,
    selected: Boolean,
    onTaskEdited: (String) -> Unit,
    onToggle: (Boolean) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CheckCircle(selected, onToggle)
        NoteItTextField(
            text = task,
            textStyle = MaterialTheme.typography.body1.copy(
                textDecoration = if (selected) TextDecoration.LineThrough else TextDecoration.None,
                color = MaterialTheme.colors.onBackground.copy(
                    alpha = if (selected) ContentAlpha.medium else ContentAlpha.high
                )
            ),
            hint = "Add task",
            onValueChanged = onTaskEdited
        )
    }

}


@Composable
private fun CheckCircle(isChecked: Boolean, onClick: (Boolean) -> Unit) {

    val modifier = if (isChecked) {

        Modifier
            .height(12.dp)
            .width(12.dp)
            .background(
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                shape = CircleShape
            )

    } else {

        Modifier
            .height(12.dp)
            .width(12.dp)
            .border(1.dp, color = MaterialTheme.colors.onSurface, shape = CircleShape)
    }
    Box(
        modifier = modifier.clickable { onClick(isChecked) }
    ) {

    }
}


@Composable
fun TextOptionsBar(onToggleList: (Boolean) -> Unit, onToggleCoverImage: (Boolean) -> Unit) {

    var listChecked by remember { mutableStateOf(false) }
    var coverImageChecked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxHeight(0.0625f)
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
    ) {
        Row {

            IconToggleButton(
                checked = coverImageChecked,
                onCheckedChange = { coverImageChecked = it; onToggleCoverImage(it) }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "add list")
            }

            IconToggleButton(
                checked = listChecked,
                onCheckedChange = { listChecked = it; onToggleList(it) }
            ) {
                Icon(Icons.Filled.List, contentDescription = "add list")
            }
        }
    }
}