package com.jesushz.notemarkmilestone.note.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.presentation.ui.asUiText
import com.jesushz.notemarkmilestone.note.domain.DefaultPaginator
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteListViewModel(
    private val sessionStorage: SessionStorage,
    private val repository: NoteRepository
) : ViewModel() {

    var hasLoadedInitialData = false

    private var paginator: DefaultPaginator<Int, Note>? = null

    private val _state = MutableStateFlow(NoteListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadNextPage()
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
        initPaginator()
    }

    fun onAction(action: NoteListAction) {
        when (action) {
            NoteListAction.LoadNextPage -> {
                loadNextPage()
            }
            else -> Unit
        }
    }

    private fun initPaginator() {
        paginator = DefaultPaginator(
            initialKey = state.value.page,
            onLoadUpdated = { isLoading ->
                _state.update {
                    it.copy(isLoading = isLoading)
                }
            },
            onRequest = { nextPage ->
                getNextPage(nextPage)
            },
            getNextKey = {
                state.value.page + 1
            },
            onError = { error ->
                withContext(Dispatchers.Main) {
                    _eventUi.send(NoteListEvent.ShowError(error.asUiText()))
                }
            },
            onSuccess = { items, newKey ->
                _state.update {
                    it.copy(
                        notes = it.notes + items,
                        page = newKey,
                        endReached = items.isEmpty()
                    )
                }
            }
        )
    }

    private fun loadNextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            paginator?.loadNextItems()
        }
    }

    private suspend fun getNextPage(nextPage: Int): Result<List<Note>, DataError.Network> {
        val pageSize = state.value.size
        return repository.getNotes(
            page = nextPage,
            pageSize = pageSize
        )
    }

}