package com.jesushz.notemarkmilestone.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jesushz.notemarkmilestone.R

@Composable
fun NoteMarkFloatingButton(
    onButtonClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onButtonClick,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.Transparent,
        contentColor = Color.White,
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF58A1F8),
                        Color(0xFF5A4CF7),
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_note)
        )
    }
}