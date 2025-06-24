package com.jesushz.notemarkmilestone.note.presentation.note_list

sealed interface NoteListAction {
    data object OnNewNoteClick : NoteListAction
    data object LoadNextPage : NoteListAction
}