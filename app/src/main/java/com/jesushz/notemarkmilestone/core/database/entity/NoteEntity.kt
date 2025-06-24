@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.notemarkmilestone.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jesushz.notemarkmilestone.core.util.toISO8601Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = Uuid.random().toString(),
    val title: String,
    val content: String,
    val createdAt: String = System.currentTimeMillis().toISO8601Duration(),
    val lastEditedAt: String = System.currentTimeMillis().toISO8601Duration()
)
