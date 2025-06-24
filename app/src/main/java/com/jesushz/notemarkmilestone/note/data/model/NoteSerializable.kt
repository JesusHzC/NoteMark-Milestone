package com.jesushz.notemarkmilestone.note.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteSerializable(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String
)
