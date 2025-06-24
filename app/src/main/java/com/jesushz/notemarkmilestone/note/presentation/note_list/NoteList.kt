package com.jesushz.notemarkmilestone.note.presentation.note_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkScaffold
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.core.presentation.ui.NoteMarkPreview
import com.jesushz.notemarkmilestone.core.util.isLandscape
import com.jesushz.notemarkmilestone.core.util.isTablet
import com.jesushz.notemarkmilestone.note.presentation.note_list.components.EmptyNotes
import com.jesushz.notemarkmilestone.note.presentation.note_list.components.NoteItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListScreenRoot(
    viewModel: NoteListViewModel = koinViewModel(),
    onCreateNewNote: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                NoteListAction.OnNewNoteClick -> onCreateNewNote()
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
    NoteMarkScaffold(
        userInitials = state.userInitials,
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
                when {
                    isTablet() -> {
                        val columns = if (isLandscape()) 3 else 2
                        var noteMaxHeight by remember { mutableIntStateOf(0) }
                        val noteMaxHeightDp = with(LocalDensity.current) { noteMaxHeight.toDp() }

                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .padding(16.dp),
                            columns = GridCells.Fixed(columns),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(
                                count = state.notes.size,
                                key = { state.notes[it].id }
                            ) { count ->
                                val item = state.notes[count]
                                if (count >= state.notes.size - 1 && !state.endReached && !state.isLoading) {
                                    onAction(NoteListAction.LoadNextPage)
                                }
                                NoteItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .defaultMinSize(minHeight = noteMaxHeightDp)
                                        .onSizeChanged { size ->
                                            noteMaxHeight = maxOf(noteMaxHeight, size.height)
                                        },
                                    note = item
                                )
                            }
                            item(
                                span = { GridItemSpan(columns) }
                            ) {
                                if (state.isLoading && state.notes.isNotEmpty()) {
                                    CircularProgressIndicator()
                                }
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
                                if (count >= state.notes.size - 1 && !state.endReached && !state.isLoading) {
                                    onAction(NoteListAction.LoadNextPage)
                                }
                                NoteItem(
                                    modifier = Modifier
                                        .wrapContentSize(),
                                    note = item
                                )
                            }
                            item(
                                span = StaggeredGridItemSpan.FullLine
                            ) {
                                if (state.isLoading && state.notes.isNotEmpty()) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
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
