package com.jesushz.notemarkmilestone.note.presentation.note_list

import com.jesushz.notemarkmilestone.core.presentation.ui.UiText

sealed interface NoteListEvent {
    data class ShowError(val error: UiText): NoteListEvent
}
