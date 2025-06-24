package com.jesushz.notemarkmilestone.note.presentation.upsert_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class UpsertNoteViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(UpsertNoteState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = UpsertNoteState()
        )

    private val _eventUi = Channel<UpsertNoteEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    fun onAction(action: UpsertNoteAction) {
        when (action) {
            else -> Unit
        }
    }

}