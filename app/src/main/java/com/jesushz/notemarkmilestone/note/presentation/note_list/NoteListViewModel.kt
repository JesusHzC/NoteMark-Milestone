package com.jesushz.notemarkmilestone.note.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.note.domain.DefaultPaginator
import com.jesushz.notemarkmilestone.note.domain.SyncNoteScheduler
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

class NoteListViewModel(
    private val sessionStorage: SessionStorage,
    private val repository: NoteRepository,
    private val syncNoteScheduler: SyncNoteScheduler
) : ViewModel() {

    private var paginator: DefaultPaginator<Int, Note>? = null

    private val _state = MutableStateFlow(NoteListState())
    val state = _state.asStateFlow()

    private val _eventUi = Channel<NoteListEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    init {
        initPaginator()
        viewModelScope.launch {
            _state.update {
                it.copy(
                    user = sessionStorage.get()?.username.orEmpty()
                )
            }
        }
        repository
            .getNotesLocalSync()
            .onEach { notes ->
                _state.update {
                    it.copy(
                        notes = notes
                    )
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            syncNoteScheduler.scheduleSync(
                type = SyncNoteScheduler.SyncType.FetchNotes(30.minutes)
            )
            repository.syncPendingNotes()
            fetchNotes()
        }
    }

    fun onAction(action: NoteListAction) {
        when (action) {
            NoteListAction.LoadNextPage -> {
                loadNextPage()
            }
            is NoteListAction.OnDeleteNote -> {
                viewModelScope.launch {
                    repository.deleteNote(action.noteId)
                }
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
            onError = {
                _state.update {
                    it.copy(
                        endReached = true
                    )
                }
            },
            onSuccess = { items, newKey ->
                _state.update {
                    it.copy(
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

    private suspend fun fetchNotes() {
        withContext(Dispatchers.IO) {
            when (val result = repository.getNotesRemoteSync(null, null)) {
                is Result.Error -> Unit
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            notes = result.data
                        )
                    }
                }
            }
        }
    }

    private suspend fun getNextPage(nextPage: Int): Result<List<Note>, DataError.Network> {
        val pageSize = state.value.size
        return repository.getNotesRemoteSync(
            page = nextPage,
            pageSize = pageSize
        )
    }

}