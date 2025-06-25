package com.jesushz.notemarkmilestone.note.presentation.note_list

import com.jesushz.notemarkmilestone.core.domain.note.Note

sealed interface NoteListAction {
    data object OnNewNoteClick : NoteListAction
    data object LoadNextPage : NoteListAction
    data class OnNoteSelected(val note: Note) : NoteListAction
}