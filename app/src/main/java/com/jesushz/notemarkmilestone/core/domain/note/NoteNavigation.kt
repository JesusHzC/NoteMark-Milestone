package com.jesushz.notemarkmilestone.core.domain.note

import kotlinx.serialization.Serializable

@Serializable
data class NoteNavigation(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String
)
