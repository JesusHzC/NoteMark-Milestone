package com.jesushz.notemarkmilestone.note.presentation.upsert_note

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.SpaceGrotesk
import com.jesushz.notemarkmilestone.core.presentation.ui.NoteMarkPreview
import com.jesushz.notemarkmilestone.core.presentation.ui.ObserveAsEvents
import com.jesushz.notemarkmilestone.note.presentation.upsert_note.components.NoteDescriptionTextField
import com.jesushz.notemarkmilestone.note.presentation.upsert_note.components.NoteTitleTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpsertNoteScreenRoot(
    viewModel: UpsertNoteViewModel = koinViewModel(),
    noteToUpdate: Note? = null,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(noteToUpdate) {
        if (noteToUpdate != null) {
            viewModel.onAction(UpsertNoteAction.OnLoadNote(noteToUpdate))
        }
    }

    ObserveAsEvents(
        flow = viewModel.eventUi
    ) { event ->
        when (event) {
            is UpsertNoteEvent.OnNoteSaved -> {
                onNavigateBack()
            }
            is UpsertNoteEvent.ShowError -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    UpsertNoteScreen(
        state = state,
        onAction = { action ->
            when (action) {
                UpsertNoteAction.OnCloseClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun UpsertNoteScreen(
    state: UpsertNoteState,
    onAction: (UpsertNoteAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onAction(UpsertNoteAction.OnCloseClick)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(
                onClick = {
                    onAction(UpsertNoteAction.OnSaveClick)
                }
            ) {
                Text(
                    text = stringResource(R.string.save_note),
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            NoteTitleTextField(
                state = state.title,
                hint = stringResource(R.string.note_title),
                modifier = Modifier
                    .fillMaxWidth()
            )
            NoteDescriptionTextField(
                state = state.description,
                hint = stringResource(R.string.note_description),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@NoteMarkPreview
@Composable
private fun UpsertNotePreview() {
    NoteMarkMilestoneTheme {
        UpsertNoteScreen(
            state = UpsertNoteState(
//                title = TextFieldState("Note Title"),
//                description = TextFieldState("Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. "),
                title = TextFieldState(""),
                description = TextFieldState("")
            ),
            onAction = {}
        )
    }
}
