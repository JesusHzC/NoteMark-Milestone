package com.jesushz.notemarkmilestone.core.data.mappers

import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.domain.note.NoteNavigation

fun Note.toNoteNavigation(): NoteNavigation {
    return NoteNavigation(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}

fun NoteNavigation.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}