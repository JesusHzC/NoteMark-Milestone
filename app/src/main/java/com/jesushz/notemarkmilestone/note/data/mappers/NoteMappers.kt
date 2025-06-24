package com.jesushz.notemarkmilestone.note.data.mappers

import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.note.data.model.NoteSerializable

fun NoteSerializable.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}

fun Note.toNoteSerializable(): NoteSerializable {
    return NoteSerializable(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}