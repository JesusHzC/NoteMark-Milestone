package com.jesushz.notemarkmilestone.core.domain.note

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String
)
