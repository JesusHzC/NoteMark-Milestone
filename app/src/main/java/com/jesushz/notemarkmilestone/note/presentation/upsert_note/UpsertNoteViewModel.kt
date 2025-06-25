package com.jesushz.notemarkmilestone.note.presentation.upsert_note

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.presentation.ui.UiText.StringResource
import com.jesushz.notemarkmilestone.core.util.toISO8601Duration
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpsertNoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UpsertNoteState())
    val state = _state.asStateFlow()

    private val _eventUi = Channel<UpsertNoteEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    fun onAction(action: UpsertNoteAction) {
        when (action) {
            is UpsertNoteAction.OnLoadNote -> {
                val note = action.note
                _state.update {
                    it.copy(
                        noteToUpdate = note,
                        title = TextFieldState(note.title),
                        description = TextFieldState(note.content)
                    )
                }
            }
            UpsertNoteAction.OnSaveClick -> {
                val title = state.value.title.text.toString()
                val description = state.value.description.text.toString()

                if (title.isBlank()) {
                    viewModelScope.launch {
                        _eventUi.send(UpsertNoteEvent.ShowError(StringResource(R.string.title_cannot_be_empty)))
                    }
                    return
                }

                if (description.isBlank()) {
                    viewModelScope.launch {
                        _eventUi.send(UpsertNoteEvent.ShowError(StringResource(R.string.description_cannot_be_empty)))
                    }
                    return
                }

                upsertNote(title, description)
            }
            UpsertNoteAction.OnCloseClick -> {
                val isUpdate = state.value.noteToUpdate != null
                val title = state.value.title.text.toString()
                val description = state.value.description.text.toString()

                val shouldShowDialog = if (isUpdate) {
                    val note = state.value.noteToUpdate!!
                    title != note.title || description != note.content
                } else {
                    title.isNotBlank() || description.isNotBlank()
                }

                if (shouldShowDialog) {
                    _state.update { it.copy(showExitDialog = true) }
                } else {
                    viewModelScope.launch {
                        _eventUi.send(UpsertNoteEvent.OnNoteSaved)
                    }
                }
            }
            UpsertNoteAction.OnDialogDismiss -> {
                _state.update {
                    it.copy(showExitDialog = false)
                }
            }
            else -> Unit
        }
    }

    private fun upsertNote(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteToUpdate = state.value.noteToUpdate
            val note = if (noteToUpdate != null) {
                _state.update {
                    it.copy(
                        noteToUpdate = it.noteToUpdate?.copy(
                            title = title,
                            content = description,
                            lastEditedAt = System.currentTimeMillis().toISO8601Duration()
                        )
                    )
                }
                state.value.noteToUpdate!!
            } else {
                Note(
                    title = title,
                    content = description
                )
            }
            repository.upsertNote(note, noteToUpdate != null)
            _eventUi.send(UpsertNoteEvent.OnNoteSaved)
        }
    }

}