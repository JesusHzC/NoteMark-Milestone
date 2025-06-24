package com.jesushz.notemarkmilestone.note.presentation.note_list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkScaffold
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.note.presentation.note_list.components.EmptyNotes
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
}

@Preview
@Composable
private fun NoteListPreview() {
    NoteMarkMilestoneTheme {
        NoteListScreen(
            state = NoteListState(
                user = "Jesus"
            ),
            onAction = {}
        )
    }
}
