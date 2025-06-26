@file:OptIn(ExperimentalMaterial3Api::class)

package com.jesushz.notemarkmilestone.note.presentation.note_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkScaffold
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.core.presentation.ui.NoteMarkPreview
import com.jesushz.notemarkmilestone.core.presentation.ui.ObserveAsEvents
import com.jesushz.notemarkmilestone.core.util.isLandscape
import com.jesushz.notemarkmilestone.core.util.isTablet
import com.jesushz.notemarkmilestone.note.presentation.note_list.components.EmptyNotes
import com.jesushz.notemarkmilestone.note.presentation.note_list.components.NoteItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListScreenRoot(
    viewModel: NoteListViewModel = koinViewModel(),
    onCreateNewNote: () -> Unit,
    onNoteSelected: (Note) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = viewModel.eventUi
    ) { event ->
        when (event) {
            is NoteListEvent.ShowError -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    NoteListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                NoteListAction.OnNewNoteClick -> onCreateNewNote()
                is NoteListAction.OnNoteSelected -> onNoteSelected(action.note)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun NoteListScreen(
    state: NoteListState,
    onAction: (NoteListAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    NoteMarkScaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        userInitials = state.userInitials,
        scrollBehavior = scrollBehavior,
        onNewNoteClick = {
            onAction(NoteListAction.OnNewNoteClick)
        },
    ) { innerPadding ->
        when {
            state.notes.isEmpty() -> {
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                                .padding(top = 80.dp)
                                .padding(horizontal = 16.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth(
                                        fraction = if (isTablet() || isLandscape()) {
                                            0.4f
                                        } else {
                                            0.6f
                                        }
                                    )
                            )
                        }
                    }
                    else -> {
                        EmptyNotes(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                                .padding(
                                    top = 80.dp
                                )
                        )
                    }
                }
            }
            else -> {
                val gridCells = if (isLandscape()) 3 else 2
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    columns = StaggeredGridCells.Fixed(gridCells),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        count = state.notes.size,
                        key = { state.notes[it].id }
                    ) { count ->
                        val item = state.notes[count]
                        /*if (count >= state.notes.size - 1 && !state.endReached && !state.isLoading) {
                            onAction(NoteListAction.LoadNextPage)
                        }*/
                        NoteItem(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            note = item,
                            onNoteClick = {
                                onAction(NoteListAction.OnNoteSelected(item))
                            },
                            onDeleteNote = {
                                onAction(NoteListAction.OnDeleteNote(item.id))
                            }
                        )
                    }
                    /*if (state.isLoading && state.page > 0 && state.notes.isNotEmpty()) {
                        item(
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            CircularProgressIndicator(
                                strokeWidth = 3.dp,
                                modifier = Modifier
                                    .requiredSize(24.dp)
                            )
                        }
                    }*/
                }
            }
        }
    }
}

@NoteMarkPreview
@Composable
private fun NoteListPreview() {
    NoteMarkMilestoneTheme {
        val stubNotes = listOf(
            Note(
                id = "1",
                title = "First Note",
                content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                lastEditedAt = "2023-10-01T12:00:00Z",
                createdAt = "2023-10-01T12:00:00Z"
            ),
            Note(
                id = "2",
                title = "Second Note",
                content = "This is the content of the second note.",
                lastEditedAt = "2023-10-02T12:00:00Z",
                createdAt = "2023-10-02T12:00:00Z"
            ),
            Note(
                id = "3",
                title = "Third Note",
                content = "This is the content of the third note.",
                lastEditedAt = "2023-10-03T12:00:00Z",
                createdAt = "2023-10-03T12:00:00Z"
            )
        )
        NoteListScreen(
            state = NoteListState(
                user = "Jesus",
                notes = stubNotes,
                isLoading = true,
            ),
            onAction = {}
        )
    }
}
