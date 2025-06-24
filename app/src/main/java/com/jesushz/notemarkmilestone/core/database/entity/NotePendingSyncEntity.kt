package com.jesushz.notemarkmilestone.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotePendingSyncEntity(
    @Embedded val note: NoteEntity,
    @PrimaryKey(autoGenerate = false)
    val noteId: String = note.id,
    val userId: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotePendingSyncEntity

        if (note != other.note) return false
        if (noteId != other.noteId) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = note.hashCode()
        result = 31 * result + noteId.hashCode()
        result = 31 * result + userId.hashCode()
        return result
    }
}
