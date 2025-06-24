package com.jesushz.notemarkmilestone.note.presentation.upsert_note

import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.presentation.ui.UiText

sealed interface UpsertNoteEvent {
    data class ShowError(val error: UiText): UpsertNoteEvent
    data class OnNoteSaved(val note: Note): UpsertNoteEvent
}
