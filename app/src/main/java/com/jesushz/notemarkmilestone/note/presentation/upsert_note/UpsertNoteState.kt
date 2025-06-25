package com.jesushz.notemarkmilestone.note.presentation.upsert_note

import androidx.compose.foundation.text.input.TextFieldState
import com.jesushz.notemarkmilestone.core.domain.note.Note

data class UpsertNoteState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
    val title: TextFieldState = TextFieldState(),
    val description: TextFieldState = TextFieldState(),
    val noteToUpdate: Note? = null,
    val showExitDialog: Boolean = false
)