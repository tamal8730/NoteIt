package com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.NoteUIModel
import com.github.tamal8730.noteit.feature_arrange_notes.view.notes_grid_screen.ui_model.TaskUIModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

private val notes = listOf(
    NoteUIModel(
        "Tasks",
        null,
        null,
        "Aug 29",
        listOf(
            TaskUIModel("Learn new things", true),
            TaskUIModel("Design things", true),
            TaskUIModel("Share my work", false),
            TaskUIModel("Stay hydrated", false),
        ),
        (0xFF5277E0).toInt(),
    ),
    NoteUIModel(
        "Tasks 2",
        "These tasks must be completed in time",
        "https://images.pexels.com/photos/13317177/pexels-photo-13317177.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        "Aug 29",
        listOf(
            TaskUIModel("Finish task one and do also da da da", true),
            TaskUIModel("Finish task two", false),
            TaskUIModel("Finish task two", false),
        ),
        null
    ),
    NoteUIModel(
        "Landscapist",
        "Landscapist is an image loading library for Jetpack Compose. There are three options; Glide, Coil, and Fresco. So you can choose by your preferences. This library also supports loading animated images such as GIFs, WebP and giving animations like shimmer effect and circular reveal. If you’d like to lookup more examples of this library, check out demo projects which use Landscapist for loading images.",
        null,
        "Sep 16",
        null,
        (0xFF8F3D67).toInt(),
    ),
    NoteUIModel(
        "Cat",
        "The cat (Felis catus) is a domestic species of small carnivorous mammal. It is the only domesticated species in the family Felidae and is often referred to as the domestic cat to distinguish it from the wild members of the family. A cat can either be a house cat, a farm cat, or a feral cat; the latter ranges freely and avoids human contact.[5] Domestic cats are valued by humans for companionship and their ability to kill rodents. About 60 cat breeds are recognized by various cat registries.",
        "https://images.pexels.com/photos/57416/cat-sweet-kitty-animals-57416.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        "Sep 12",
        null,
        null
    ),
)

@Composable
fun NotesGridScreen() {

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
            FloatingActionButton(onClick = {}) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "New note"
                )
            }

        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notes.size) { index ->
                    Note(note = notes[index])
                }
            }
        }
    }

}

@Composable
private fun Note(note: NoteUIModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .background(
                if (note.color != null) Color(note.color) else MaterialTheme.colors.surface,
                shape = RoundedCornerShape(13)
            )
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
fun TaskListItem(taskUIModel: TaskUIModel) {
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
private fun CoverImage(imageURL: String) {
    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(13)),
        imageModel = imageURL,
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop
        )
    )
}
