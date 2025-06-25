@file:OptIn(ExperimentalMaterial3Api::class)

package com.jesushz.notemarkmilestone.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NoteMarkScaffold(
    modifier: Modifier = Modifier,
    userInitials: String,
    onNewNoteClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            NoteMarkTopBar(
                userInitials = userInitials,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            NoteMarkFloatingButton(
                onButtonClick = onNewNoteClick
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        content(innerPadding)
    }
}