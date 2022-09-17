package com.github.tamal8730.noteit.core.data.db

import com.github.tamal8730.noteit.core.model.NoteModel
import com.github.tamal8730.noteit.core.model.TaskListItemModel
import kotlinx.coroutines.delay

object FakeDB {

    private val notes = mutableMapOf<String, NoteModel>(
        "0" to NoteModel(
            id = "0",
            title = "Sita Ramam",
            body = "Sita Ramam is a 2022 Indian Telugu-language period romantic drama film written and directed by Hanu Raghavapudi and produced by Vyjayanthi Movies and Swapna Cinema. The film stars Dulquer Salmaan, Mrunal Thakur (in her Telugu debut), Rashmika Mandanna and Sumanth. Set in 1964, Lieutenant Ram, an orphan Indian army officer serving at the Kashmir border, gets anonymous love letters from Sita Mahalakshmi. Ram is on a mission to find Sita and propose his love.",
            coverImage = "content://com.android.providers.media.documents/document/image%3A149673",
            lastModifiedAt = "now",
            tasks = null,
            color = null,
        ),
        "1" to NoteModel(
            id = "1",
            title = "Bhanu Bandopadhyay",
            body = "Bhanu Bandyopadhyay, also known as Bhanu Banerjee (born as Samyamoy Bandyopadhyay; 26 August 1920[1] – 4 March 1983), was an Indian actor, known for his work in Bengali cinema. He acted in over 300 movies, in numerous plays and performed frequently on the radio.",
            coverImage = null,
            lastModifiedAt = "now",
            tasks = listOf(
                TaskListItemModel("Water the plants", true),
                TaskListItemModel("Kill the weeds", true),
                TaskListItemModel("Kill the plants", false),
                TaskListItemModel("Cry over the loss", false),
            ),
            color = null,
        )
    )

    suspend fun addNote(note: NoteModel) {
        delay(1000)
        val key = (notes.size + 1).toString()
        notes[key] = note
    }

    suspend fun getNote(id: String): NoteModel? {
        delay(1000)
        return notes[id]
    }

    suspend fun getAllNotes(): List<NoteModel> {
        delay(1000)
        return notes.map { it.value }
    }

}