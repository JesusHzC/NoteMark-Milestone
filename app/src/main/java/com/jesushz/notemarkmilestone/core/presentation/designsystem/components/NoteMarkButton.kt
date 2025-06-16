package com.jesushz.notemarkmilestone.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoteMarkButton(
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    onButtonClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        modifier = modifier,
        enabled = isEnable,
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(12.dp),
        content = content,
    )
}
