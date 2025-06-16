package com.jesushz.notemarkmilestone.auth.presentation.intro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jesushz.notemarkmilestone.auth.presentation.intro.components.ContentLandscape
import com.jesushz.notemarkmilestone.auth.presentation.intro.components.ContentPortrait
import com.jesushz.notemarkmilestone.auth.presentation.intro.components.ContentTablet
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.core.presentation.ui.NoteMarkPreview
import com.jesushz.notemarkmilestone.core.presentation.ui.rememberDeviceInfo

@Composable
fun IntroScreenRoot(
    navigateToGetStarted: () -> Unit,
    navigateToLogin: () -> Unit
) {
    IntroScreen(
        onAction = { action ->
            when (action) {
                IntroAction.OnGetStarted -> navigateToGetStarted()
                IntroAction.OnLogIn -> navigateToLogin()
            }
        }
    )
}

@Composable
private fun IntroScreen(
    onAction: (IntroAction) -> Unit,
) {
    val deviceInfo = rememberDeviceInfo()

    when {
        deviceInfo.isTablet -> {
            ContentTablet(
                modifier = Modifier
                    .fillMaxSize(),
                onGetStartedClick = {
                    onAction(IntroAction.OnGetStarted)
                },
                onLoginClick = {
                    onAction(IntroAction.OnLogIn)
                }
            )
        }
        deviceInfo.isLandscape -> {
            ContentLandscape(
                modifier = Modifier
                    .fillMaxSize(),
                onGetStartedClick = {
                    onAction(IntroAction.OnGetStarted)
                },
                onLoginClick = {
                    onAction(IntroAction.OnLogIn)
                }
            )
        }
        else -> {
            ContentPortrait(
                modifier = Modifier
                    .fillMaxSize(),
                onGetStartedClick = {
                    onAction(IntroAction.OnGetStarted)
                },
                onLoginClick = {
                    onAction(IntroAction.OnLogIn)
                }
            )
        }
    }
}

@NoteMarkPreview
@Composable
private fun IntroPreview() {
    NoteMarkMilestoneTheme {
        IntroScreen(
            onAction = {}
        )
    }
}
