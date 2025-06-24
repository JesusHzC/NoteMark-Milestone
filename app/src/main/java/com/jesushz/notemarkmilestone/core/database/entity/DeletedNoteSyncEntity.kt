package com.jesushz.notemarkmilestone.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeletedNoteSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val noteId: String,
    val userId: String
)
