package com.jesushz.notemarkmilestone.note.presentation.upsert_note

sealed interface UpsertNoteAction {
    data object OnCloseClick : UpsertNoteAction
    data object OnSaveClick : UpsertNoteAction
}