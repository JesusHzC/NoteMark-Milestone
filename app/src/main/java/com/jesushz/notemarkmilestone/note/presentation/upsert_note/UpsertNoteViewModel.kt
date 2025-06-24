package com.jesushz.notemarkmilestone.note.presentation.upsert_note

import androidx.appcompat.widget.DialogTitle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.presentation.ui.UiText
import com.jesushz.notemarkmilestone.core.presentation.ui.asUiText
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpsertNoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UpsertNoteState())
    val state = _state.asStateFlow()

    private val _eventUi = Channel<UpsertNoteEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    fun onAction(action: UpsertNoteAction) {
        when (action) {
            UpsertNoteAction.OnSaveClick -> {
                val title = state.value.title.text.toString()
                val description = state.value.description.text.toString()

                if (title.isBlank()) {
                    viewModelScope.launch {
                        _eventUi.send(UpsertNoteEvent.ShowError(UiText.StringResource(R.string.title_cannot_be_empty)))
                    }
                    return
                }

                if (description.isBlank()) {
                    viewModelScope.launch {
                        _eventUi.send(UpsertNoteEvent.ShowError(UiText.StringResource(R.string.description_cannot_be_empty)))
                    }
                    return
                }

                createNote(title, description)
            }
            else -> Unit
        }
    }

    private fun createNote(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.createNote(title, description)) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        _eventUi.send(UpsertNoteEvent.OnNoteSaved(result.data))
                    }
                }
                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        _eventUi.send(UpsertNoteEvent.ShowError(result.error.asUiText()))
                    }
                }
            }
        }
    }

}