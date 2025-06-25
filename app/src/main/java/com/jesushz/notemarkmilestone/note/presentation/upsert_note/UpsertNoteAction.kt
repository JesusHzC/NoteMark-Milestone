package com.jesushz.notemarkmilestone.note.presentation.upsert_note

import com.jesushz.notemarkmilestone.core.domain.note.Note

sealed interface UpsertNoteAction {
    data class OnLoadNote(val note: Note) : UpsertNoteAction
    data object OnCloseClick : UpsertNoteAction
    data object OnSaveClick : UpsertNoteAction
    data object OnDialogDismiss : UpsertNoteAction
    data object OnDialogAccept : UpsertNoteAction
}
