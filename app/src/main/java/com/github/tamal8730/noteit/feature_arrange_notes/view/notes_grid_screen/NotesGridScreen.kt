package com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.NoteUIModel
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.TaskUIModel
import com.github.tamal8730.noteit.feature_arrange_notes.view_model.NotesGridScreenUIState
import com.github.tamal8730.noteit.feature_arrange_notes.view_model.NotesGridScreenViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NotesGridScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: NotesGridScreenViewModel,
    onEditNote: (id: Long) -> Unit,
    onCreateNewNote: () -> Unit,
) {

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadAllNotes()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    }

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "NoteIt", fontWeight = FontWeight.Bold) },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            if (uiState is NotesGridScreenUIState.Loaded) {
                FloatingActionButton(onClick = { onCreateNewNote() }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "New note"
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {

            when (uiState) {

                is NotesGridScreenUIState.Empty -> {

                }

                is NotesGridScreenUIState.Loaded -> {

                    val notes = uiState.notes

                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(notes.size) { index ->
                            Note(note = notes[index]) { onEditNote(notes[index].id) }
                        }
                    }

                }

                is NotesGridScreenUIState.Loading -> {
                    Loading()
                }

                is NotesGridScreenUIState.NoNotes -> {
                    NoNotes { onCreateNewNote() }
                }
            }


        }
    }

}


@Composable
private fun NoNotes(onCreateNewNote: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "You have no saved notes. Create your first one by tapping the button below",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onCreateNewNote) {
                Text(text = "Create new note")
            }
        }

    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Note(note: NoteUIModel, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .background(
                if (note.color != null) Color(note.color) else MaterialTheme.colors.surface,
                shape = RoundedCornerShape(13)
            )
            .clickable { onClick() }
    ) {
        Column {
            if (note.coverImage != null) {
                CoverImage(note.coverImage)
            }
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {

                Text(
                    note.title,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (note.body != null) {
                    Text(
                        note.body, maxLines = 5, overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp,
                        style = MaterialTheme.typography.body2.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (note.tasks != null && note.tasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TaskList(note.tasks)
                    Spacer(modifier = Modifier.height(24.dp))
                }
                Text(
                    note.lastModifiedAt,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.subtitle2
                        .copy(
                            color = MaterialTheme.colors.onSurface.copy(
                                alpha = ContentAlpha.disabled
                            )
                        )
                )
            }

        }
    }
}

@Composable
private fun TaskListItem(taskUIModel: TaskUIModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CheckCircle(taskUIModel.complete)
        Text(
            text = taskUIModel.task,
            style = MaterialTheme.typography.body2.copy(
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = if (taskUIModel.complete) ContentAlpha.medium
                    else ContentAlpha.high
                ),
                textDecoration = if (taskUIModel.complete) TextDecoration.LineThrough
                else TextDecoration.None
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CheckCircle(isChecked: Boolean) {
    val modifier = if (isChecked) {

        Modifier
            .height(12.dp)
            .aspectRatio(1f)
            .background(
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                shape = CircleShape
            )

    } else {

        Modifier
            .height(12.dp)
            .aspectRatio(1f)
            .border(1.dp, color = MaterialTheme.colors.onSurface, shape = CircleShape)
    }
    Box(
        modifier = modifier
    ) {

    }
}

@Composable
private fun TaskList(tasks: List<TaskUIModel>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        tasks.map {
            TaskListItem(it)
        }
    }
}

@Composable
private fun CoverImage(imageUri: Uri) {
    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(13)),
        imageModel = imageUri,
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop
        )
    )
}
