package com.jesushz.notemarkmilestone.note.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotesResponse(
    val notes: List<NoteSerializable> = emptyList(),
    val total: Int
)
