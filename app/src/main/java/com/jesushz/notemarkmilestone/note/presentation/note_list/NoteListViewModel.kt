package com.jesushz.notemarkmilestone.note.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(NoteListState())
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
            initialValue = NoteListState()
        )

    private val _eventUi = Channel<NoteListEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    user = sessionStorage.get()?.username.orEmpty()
                )
            }
        }
    }

    fun onAction(action: NoteListAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}