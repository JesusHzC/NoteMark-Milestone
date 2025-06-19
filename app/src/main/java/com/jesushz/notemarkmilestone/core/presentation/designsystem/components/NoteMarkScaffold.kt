package com.jesushz.notemarkmilestone.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun NoteMarkScaffold(
    userInitials: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            NoteMarkTopBar(
                userInitials = userInitials
            )
        },
        floatingActionButton = {
            NoteMarkFloatingButton(
                onButtonClick = {}
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        content(innerPadding)
    }
}