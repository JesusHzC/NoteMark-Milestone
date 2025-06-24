package com.jesushz.notemarkmilestone.core.database.mappers

import com.jesushz.notemarkmilestone.core.database.entity.NoteEntity
import com.jesushz.notemarkmilestone.core.domain.note.Note

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}
