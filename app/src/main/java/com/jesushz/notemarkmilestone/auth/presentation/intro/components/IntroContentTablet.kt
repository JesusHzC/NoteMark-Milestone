package com.jesushz.notemarkmilestone.auth.presentation.intro.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkButton
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkButtonOutlined
import com.jesushz.notemarkmilestone.core.util.isLandscape

@Composable
internal fun IntroContentTablet(
    modifier: Modifier = Modifier,
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    when {
        isLandscape() -> {
            ContentLandscape(
                modifier = modifier,
                onGetStartedClick = onGetStartedClick,
                onLoginClick = onLoginClick
            )
        }
        else -> {
            ContentPortrait(
                modifier = modifier,
                onGetStartedClick = onGetStartedClick,
                onLoginClick = onLoginClick
            )
        }
    }
}

@Composable
private fun ContentLandscape(
    modifier: Modifier = Modifier,
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_background_intro_landscape),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(
                    color = Color(0xFFE0EAFF)
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(
                        top = 40.dp,
                        bottom = 40.dp,
                        start = 15.dp,
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    bottomStart = 20.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            top = 40.dp,
                            bottom = 40.dp,
                            end = 40.dp,
                            start = 60.dp
                        )
                ) {
                    Text(
                        text = stringResource(R.string.intro_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.intro_title),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    NoteMarkButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onButtonClick = onGetStartedClick
                    ) {
                        Text(
                            text = stringResource(R.string.get_started),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    NoteMarkButtonOutlined(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onButtonClick = onLoginClick
                    ) {
                        Text(
                            text = stringResource(R.string.log_in),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentPortrait(
    modifier: Modifier = Modifier,
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_background_intro),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 48.dp,
                        vertical = 22.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.intro_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.intro_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(40.dp))
                NoteMarkButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onButtonClick = onGetStartedClick
                ) {
                    Text(
                        text = stringResource(R.string.get_started),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                NoteMarkButtonOutlined(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onButtonClick = onLoginClick
                ) {
                    Text(
                        text = stringResource(R.string.log_in),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}
