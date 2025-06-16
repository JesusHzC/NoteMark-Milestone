package com.jesushz.notemarkmilestone.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun NoteMarkButtonOutlined(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onButtonClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(12.dp),
        border = ButtonDefaults.outlinedButtonBorder(
            enabled = true,
        ).copy(
            width = 1.dp,
            brush = SolidColor(MaterialTheme.colorScheme.primary)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        content = content,
    )
}
